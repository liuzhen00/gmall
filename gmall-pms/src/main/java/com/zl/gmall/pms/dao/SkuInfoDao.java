package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * sku信息
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:17
 */
@Mapper
@Repository
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
	
}
