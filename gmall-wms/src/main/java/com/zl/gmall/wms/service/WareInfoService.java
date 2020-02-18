package com.zl.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.wms.entity.WareInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 仓库信息
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:36:37
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

