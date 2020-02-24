package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.SpuInfoDescEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * spu信息介绍
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:16
 */
@Mapper
@Repository
public interface SpuInfoDescDao extends BaseMapper<SpuInfoDescEntity> {
	
}
