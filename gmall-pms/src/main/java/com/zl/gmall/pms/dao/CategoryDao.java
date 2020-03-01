package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.gmall.pms.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:18
 */
@Mapper
@Repository
public interface CategoryDao extends BaseMapper<CategoryEntity> {

    List<CategoryVo> queryCategoryLeve2Andleve3ByPid(Long pid);
	
}
