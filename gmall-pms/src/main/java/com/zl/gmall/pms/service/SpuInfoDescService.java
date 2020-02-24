package com.zl.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.zl.gmall.pms.vo.SpuInfoVo;


/**
 * spu信息介绍
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:16
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageVo queryPage(QueryCondition params);

    public Long saveSpuInfoDesc(SpuInfoVo spuInfoVo);
}

