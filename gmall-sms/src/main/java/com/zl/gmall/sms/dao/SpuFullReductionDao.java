package com.zl.gmall.sms.dao;

import com.zl.gmall.sms.entity.SpuFullReductionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品满减信息
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:48:07
 */
@Mapper
@Repository
public interface SpuFullReductionDao extends BaseMapper<SpuFullReductionEntity> {
	
}
