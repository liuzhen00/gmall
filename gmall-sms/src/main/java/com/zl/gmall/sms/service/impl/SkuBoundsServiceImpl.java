package com.zl.gmall.sms.service.impl;

import com.zl.gmall.sms.dao.SkuFullReductionDao;
import com.zl.gmall.sms.dao.SkuLadderDao;
import com.zl.gmall.sms.entity.SkuFullReductionEntity;
import com.zl.gmall.sms.entity.SkuLadderEntity;
import com.zl.gmall.sms.vo.SaleVo;
import com.zl.gmall.sms.vo.SkuSaleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.sms.dao.SkuBoundsDao;
import com.zl.gmall.sms.entity.SkuBoundsEntity;
import com.zl.gmall.sms.service.SkuBoundsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sound.midi.SoundbankResource;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {
    @Autowired
    private SkuFullReductionDao skuFullReductionDao;

    @Autowired
    private SkuLadderDao skuLadderDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }
    //保存SKU的营销信息
    @Transactional
    @Override
    public void saveSkuInfo(SkuSaleDTO saleDTO) {
        //积分优惠
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        BeanUtils.copyProperties(saleDTO,skuBoundsEntity);
        List<Integer> work = saleDTO.getWork();
        if(!CollectionUtils.isEmpty(work)){
            skuBoundsEntity.setWork(work.get(0)*8+work.get(1)*4+work.get(2)*2+work.get(3));
        }
        System.out.println("skuBoundsEntity:"+skuBoundsEntity);
        this.save(skuBoundsEntity);

        //满减优惠
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(saleDTO,skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(saleDTO.getFullAddOther());
        skuFullReductionDao.insert(skuFullReductionEntity);
        System.out.println("skuBoundsEntity:"+skuFullReductionEntity);

        //数量折扣
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(saleDTO,skuLadderEntity);
        skuLadderDao.insert(skuLadderEntity);

    }

    @Override
    public List<SaleVo> querySkuBySaleInfo(Long skuId) {
        List<SaleVo> saleVoList = new ArrayList<>();

        //查询积分信息
        SkuBoundsEntity skuBoundsEntity = this.getOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        if(skuBoundsEntity!=null){
            SaleVo saleBounds = new SaleVo();
            saleBounds.setType("积分");
            saleBounds.setDesc("成长积分赠送"+skuBoundsEntity.getBuyBounds()+"购物积分赠送"+skuBoundsEntity.getBuyBounds());
            saleVoList.add(saleBounds);
        }

        //查询满减
        SkuFullReductionEntity skuFullReductionEntity = this.skuFullReductionDao.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
        if(skuFullReductionEntity!=null){
            SaleVo saleVoFull = new SaleVo();
            saleVoFull.setType("满减");
            saleVoFull.setDesc("满"+skuFullReductionEntity.getFullPrice()+"减"+skuFullReductionEntity.getReducePrice());
            saleVoList.add(saleVoFull);
        }
        //设计打折信息
        SkuLadderEntity skuLadderEntity = this.skuLadderDao.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        if(skuLadderEntity!=null){
            SaleVo saleVoFull = new SaleVo();
            saleVoFull.setType("打折");
            saleVoFull.setDesc("满"+skuLadderEntity.getFullCount()+"件打"+skuLadderEntity.getDiscount().divide(new BigDecimal(10))+"折");
            saleVoList.add(saleVoFull);
        }
        return saleVoList;
    }

}