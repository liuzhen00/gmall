package com.zl.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.oms.entity.PaymentInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 支付信息表
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:59:33
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

