package com.zl.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.zl.gmall.sms.vo.SaleVo;
import com.zl.gmall.sms.vo.SkuSaleDTO;

import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:48:06
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

    void saveSkuInfo(SkuSaleDTO saleDTO);

    List<SaleVo> querySkuBySaleInfo(Long skuId);
}

