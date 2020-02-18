package com.zl.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.oms.entity.OrderReturnReasonEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 退货原因
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:59:32
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageVo queryPage(QueryCondition params);
}

