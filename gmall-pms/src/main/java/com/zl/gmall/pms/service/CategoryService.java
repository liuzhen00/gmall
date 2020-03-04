package com.zl.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.zl.gmall.pms.vo.CategoryVo;
import com.zl.gmall.pms.vo.ItemGroupVo;

import java.util.List;


/**
 * 商品三级分类
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:18
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageVo queryPage(QueryCondition params);

    List<CategoryEntity> queryCategoryByLebelorPid(Integer level, Long pid);

    List<CategoryVo> queryCategoryByLebe2ByPid(Long pid);


}

