package com.zl.gmall.pms.dao;

import com.zl.gmall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * spu属性值
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:17
 */
@Mapper
@Repository
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {
    List<ProductAttrValueEntity> querySearchAttrValue(Long spuId);
	
}
