package com.zl.gmall.oms.dao;

import com.zl.gmall.oms.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 订单项信息
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:59:32
 */
@Mapper
@Repository
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
