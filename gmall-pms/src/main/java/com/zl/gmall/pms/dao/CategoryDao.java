package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:18
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
