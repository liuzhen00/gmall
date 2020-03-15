package com.zl.gmall.wms.dao;

import com.zl.gmall.wms.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品库存
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:36:37
 */
@Mapper
@Repository
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    //验库存
    List<WareSkuEntity> checkStock(@Param("skuId")Long skuId,@Param("count")Integer count);
    //锁库存
    int lockStock(@Param("id")Long id,@Param("count")Integer count);
    //解库存
    int unlockStock(@Param("id")Long id,@Param("count")Integer count);

    //减库存
    int minusStock(@Param("id")Long id,@Param("count")Integer count);
}
