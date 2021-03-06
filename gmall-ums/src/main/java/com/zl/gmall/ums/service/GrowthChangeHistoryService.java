package com.zl.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.ums.entity.GrowthChangeHistoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 成长值变化历史记录
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:42:00
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

