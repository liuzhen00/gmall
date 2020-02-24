package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 属性&属性分组关联
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:17
 */
@Mapper
@Repository
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
	
}
