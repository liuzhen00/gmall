package com.zl.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.sms.entity.CouponHistoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 优惠券领取历史记录
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:48:06
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

