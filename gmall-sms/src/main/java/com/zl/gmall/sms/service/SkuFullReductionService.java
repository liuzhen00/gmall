package com.zl.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品满减信息
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:48:07
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageVo queryPage(QueryCondition params);
}

