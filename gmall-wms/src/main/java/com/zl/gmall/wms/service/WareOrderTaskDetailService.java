package com.zl.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.wms.entity.WareOrderTaskDetailEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 库存工作单
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:36:38
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageVo queryPage(QueryCondition params);
}

