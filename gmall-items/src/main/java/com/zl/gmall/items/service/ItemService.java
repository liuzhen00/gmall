package com.zl.gmall.items.service;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.items.vo.ItemVo;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.pms.feign.GmallPmsApi;
import com.zl.gmall.pms.vo.ItemGroupVo;
import com.zl.gmall.sms.feign.GmallSmsApi;
import com.zl.gmall.sms.vo.SaleVo;
import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.feign.GmallWmsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-03 22:47
 */
@Service
public class ItemService {

    @Autowired
    private GmallPmsApi gmallPmsApi;

    @Autowired
    private GmallSmsApi gmallSmsApi;

    @Autowired
    private GmallWmsApi gmallWmsApi;

    public ItemVo queryItemBySkuId(Long skuId) {

        ItemVo itemVo=new ItemVo();
        itemVo.setSkuId(skuId);

        //查询sku的基本信息

        Resp<SkuInfoEntity> skuInfoEntityResp = this.gmallPmsApi.querySkuById(skuId);
        SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
        if(skuInfoEntity==null){
            return null;
        }
        itemVo.setPrice(skuInfoEntity.getPrice());
        itemVo.setWeight(skuInfoEntity.getWeight());
        itemVo.setSkuSubTitle(skuInfoEntity.getSkuSubtitle());
        itemVo.setSkuTitle(skuInfoEntity.getSkuTitle());

        //分类信息
        Resp<CategoryEntity> categoryEntityResp = this.gmallPmsApi.info(skuInfoEntity.getCatalogId());
        CategoryEntity categoryEntity = categoryEntityResp.getData();
        if(categoryEntity!=null){
            itemVo.setCategoryId(skuInfoEntity.getCatalogId());
            itemVo.setCategoryName(categoryEntity.getName());
        }
        //根据品牌ID查询
        Resp<BrandEntity> brandEntityResp = this.gmallPmsApi.queryByBrandId(skuInfoEntity.getBrandId());
        BrandEntity brandEntity = brandEntityResp.getData();
        if(brandEntity!=null){
            itemVo.setBrandId(skuInfoEntity.getBrandId());
            itemVo.setBrandName(brandEntity.getName());
        }

        //根据spu查询spu的信息
        Resp<SpuInfoEntity> spuInfoEntityResp = this.gmallPmsApi.querySpuById(skuInfoEntity.getSpuId());
        SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
        if(spuInfoEntity!=null){
            itemVo.setSpuId(skuInfoEntity.getSpuId());
            itemVo.setSpuName(spuInfoEntity.getSpuName());
        }
        //根据skuid查询营销信息
        Resp<List<SaleVo>> skuBySaleInfo = this.gmallSmsApi.querySkuBySaleInfo(skuId);
        List<SaleVo> saleVos = skuBySaleInfo.getData();
        if(!CollectionUtils.isEmpty(saleVos)){
            itemVo.setSales(saleVos);
        }

        //根据Sku查询库存
        Resp<List<WareSkuEntity>> wareSkuEntityResp = this.gmallWmsApi.queryWareSku(skuId);
        List<WareSkuEntity> wareSkuEntities = wareSkuEntityResp.getData();
        if(!CollectionUtils.isEmpty(wareSkuEntities)){
            itemVo.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock()>0));
        }

        //根据Spu查询所有的销售属性
        Resp<List<SkuSaleAttrValueEntity>> skuSaleBySpuId = this.gmallPmsApi.querySkuSaleBySpuId(skuInfoEntity.getSpuId());
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleBySpuId.getData();
        if(!CollectionUtils.isEmpty(skuSaleAttrValueEntities)){
            itemVo.setSaleAttrs(skuSaleAttrValueEntities);
        }

        //根据SkuId查询Sku的图片信息
        Resp<List<SkuImagesEntity>> skuImagesListResp = this.gmallPmsApi.querySkuImageById(skuId);
        List<SkuImagesEntity> skuImagesEntities = skuImagesListResp.getData();
        if(!CollectionUtils.isEmpty(skuImagesEntities)){
            itemVo.setImages(skuImagesEntities);
        }

        //根据spu添加描述信息
        Resp<SpuInfoDescEntity> spuInfoDescEntityResp = this.gmallPmsApi.querySpuDescById(spuInfoEntity.getId());
        SpuInfoDescEntity descEntity = spuInfoDescEntityResp.getData();
        if(descEntity!=null){
            String[] split = descEntity.getDecript().split(",");
            itemVo.setDesc(Arrays.asList(split));
        }

        //根据分类ID查询规格参数
        Resp<List<ItemGroupVo>> listResp = this.gmallPmsApi.queryItemGroupVoById(spuInfoEntity.getId(),categoryEntity.getCatId());
        List<ItemGroupVo> itemGroupVos = listResp.getData();
        if(!CollectionUtils.isEmpty(itemGroupVos)){
            itemVo.setAttrGroups(itemGroupVos);
        }

        return itemVo;

    }
}
