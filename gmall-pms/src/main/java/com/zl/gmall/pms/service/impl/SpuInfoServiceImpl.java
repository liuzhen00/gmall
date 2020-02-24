package com.zl.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zl.gmall.pms.dao.*;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.pms.feign.SkuSaleFeign;
import com.zl.gmall.pms.service.ProductAttrValueService;
import com.zl.gmall.pms.service.SkuImagesService;
import com.zl.gmall.pms.service.SpuInfoDescService;
import com.zl.gmall.pms.vo.ProduceAttrValueVo;
import com.zl.gmall.pms.vo.SkuInfoVo;
import com.zl.gmall.pms.vo.SpuInfoVo;
import com.zl.gmall.sms.vo.SkuSaleDTO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;

    @Autowired
    private ProductAttrValueService productAttrValueService;
    
    @Autowired
    private SkuInfoDao skuInfoDao;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    private SkuSaleFeign skuSaleFeign;

    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfo(QueryCondition queryCondition, Long cid) {

        QueryWrapper<SpuInfoEntity> wrapper=new QueryWrapper();
        if(cid!=0){
            wrapper.eq("catalog_id",cid);
        }

        String key = queryCondition.getKey();
        if(StringUtils.isNotBlank(key)){
            wrapper.and(t->t.eq("id",key).or().like("spu_name",key));
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(queryCondition),wrapper
        );

        return new PageVo(page);

    }

    @GlobalTransactional
    @Override
    public void spuInfoVo(SpuInfoVo spuInfoVo) {
        //1、保存spu相关的信息
        //1.1保存SPU的基本信息 spuInfo
        saveBaseSpuInfo(spuInfoVo);
        Long spuId =this.spuInfoDescService.saveSpuInfoDesc(spuInfoVo);

       // int i=1/0;
        //1.3保存SPU的规格参数信息
        saveSpuBaseAttr(spuInfoVo, spuId);

        //2.保存Sku的相关信息
        List<SkuInfoVo> skusVo = spuInfoVo.getSkus();
        if(CollectionUtils.isEmpty(skusVo)){
            return;
        }
        saveSkuInfoAndSaleInfo(spuInfoVo, spuId, skusVo);


    }

    private void saveSkuInfoAndSaleInfo(SpuInfoVo spuInfoVo, Long spuId, List<SkuInfoVo> skusVo) {
        //2.1保存sku的基本信息
        skusVo.forEach(baseSkuInfos -> {
             //保存基本信息
             SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
             BeanUtils.copyProperties(baseSkuInfos, skuInfoEntity);
             skuInfoEntity.setBrandId(spuInfoVo.getBrandId());
             skuInfoEntity.setCatalogId(spuInfoVo.getCatalogId());
             //获取随机的UUID作为SKU的编码
             skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
             //获取图片列表
             List<String> images = baseSkuInfos.getImages();
             if (!CollectionUtils.isEmpty(images)) { //如果图片列表不为空，则设置，默认图片
                 skuInfoEntity.setSkuDefaultImg(
                         skuInfoEntity.getSkuDefaultImg() == null ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
             }
             skuInfoEntity.setSpuId(spuId);
             skuInfoDao.insert(skuInfoEntity);//基本属性插入完成
             System.out.println("基本信息插入完成");
             //获取SKU的ID
             Long skuId = skuInfoEntity.getSkuId();

             //2.2保存Sku的图片信息
             if (!CollectionUtils.isEmpty(images)) {
                 String defaultImge = images.get(0);
                 List<SkuImagesEntity> skuImages = images.stream().map(image -> {
                     SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                     skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImge, image) ? 1 : 0);
                     skuImagesEntity.setSkuId(skuId);
                     skuImagesEntity.setImgSort(0);
                     skuImagesEntity.setImgUrl(image);
                     return skuImagesEntity;
                 }).collect(Collectors.toList());
                 skuImagesService.saveBatch(skuImages);
             }
             //2.3保存sku的规格参数信息
             List<SkuSaleAttrValueEntity> saleAttrs = baseSkuInfos.getSaleAttrs();
            if (!CollectionUtils.isEmpty(saleAttrs)){
                saleAttrs.forEach(skuSaleAttrValueEntity -> {
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    this.skuSaleAttrValueDao.insert(skuSaleAttrValueEntity);
                });
            }
             System.out.println("营销信息办成完成");
             //保存营销相关信息
             SkuSaleDTO saleDTO = new SkuSaleDTO();
             BeanUtils.copyProperties(baseSkuInfos, saleDTO);
            System.out.println("saleDTO"+saleDTO);
             saleDTO.setSkuId(skuId);
            this.skuSaleFeign.saveSkuSaleInfo(saleDTO);
         });
    }

    private void saveSpuBaseAttr(SpuInfoVo spuInfoVo, Long spuId) {
        List<ProduceAttrValueVo> baseAttrs = spuInfoVo.getBaseAttrs();
        if(!CollectionUtils.isEmpty(baseAttrs)){
            List<ProductAttrValueEntity> produceAttrValueVos = baseAttrs.stream().map(productAttrVo -> {
                productAttrVo.setSpuId(spuId);
                productAttrVo.setAttrSort(0);
                productAttrVo.setQuickShow(0);
                return productAttrVo;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(produceAttrValueVos);
        }
    }



    private void saveBaseSpuInfo(SpuInfoVo spuInfoVo) {
        spuInfoVo.setPublishStatus(1);//默认已经上架
        spuInfoVo.setCreateTime(new Date());
        spuInfoVo.setUodateTime(spuInfoVo.getCreateTime());    //更新和创建时间一样
        this.save(spuInfoVo);
    }

}