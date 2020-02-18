package com.zl.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.sms.entity.CategoryBoundsEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品分类积分设置
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:48:06
 */
public interface CategoryBoundsService extends IService<CategoryBoundsEntity> {

    PageVo queryPage(QueryCondition params);
}

