package com.zl.gmall.pms.service.impl;

import com.zl.gmall.pms.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.pms.dao.CategoryDao;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.zl.gmall.pms.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
   private CategoryDao categoryDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public List<CategoryEntity> queryCategoryByLebelorPid(Integer level, Long pid) {
        QueryWrapper qrWrapper=new QueryWrapper();
        if(level!=0){
            qrWrapper.eq("cat_level",level);
        }

        if(pid!=null){
            qrWrapper.eq("parent_cid",pid);
        }

        return this.list(qrWrapper);
    }

    @Override
    public List<CategoryVo> queryCategoryByLebe2ByPid(Long pid) {
        return categoryDao.queryCategoryLeve2Andleve3ByPid(pid);
    }

}