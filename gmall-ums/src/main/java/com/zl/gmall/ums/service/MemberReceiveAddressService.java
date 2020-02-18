package com.zl.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员收货地址
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:42:01
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageVo queryPage(QueryCondition params);
}

