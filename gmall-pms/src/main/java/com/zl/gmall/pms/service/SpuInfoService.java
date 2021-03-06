package com.zl.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.zl.gmall.pms.vo.SpuInfoVo;


/**
 * spu信息
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:17
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo querySpuInfo(QueryCondition queryCondition, Long cid);

    void spuInfoVo(SpuInfoVo spuInfoVo);
}

