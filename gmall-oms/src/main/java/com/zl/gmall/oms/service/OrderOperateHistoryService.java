package com.zl.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.oms.entity.OrderOperateHistoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 订单操作历史记录
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:59:32
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

