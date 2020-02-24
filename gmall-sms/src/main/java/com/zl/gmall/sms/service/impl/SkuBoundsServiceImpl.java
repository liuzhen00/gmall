package com.zl.gmall.sms.service.impl;

import com.zl.gmall.sms.dao.SkuFullReductionDao;
import com.zl.gmall.sms.dao.SkuLadderDao;
import com.zl.gmall.sms.entity.SkuFullReductionEntity;
import com.zl.gmall.sms.entity.SkuLadderEntity;
import com.zl.gmall.sms.vo.SkuSaleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}