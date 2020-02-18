package com.zl.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.ums.entity.MemberCollectSubjectEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员收藏的专题活动
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:42:01
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageVo queryPage(QueryCondition params);
}

