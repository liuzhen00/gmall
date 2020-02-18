package com.zl.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.ums.entity.MemberLevelEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员等级
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:42:01
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageVo queryPage(QueryCondition params);
}

