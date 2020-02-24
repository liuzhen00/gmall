package com.zl.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.zl.gmall.pms.vo.AttrGroupVo;

import java.util.List;


/**
 * 属性分组
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:16
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryGroupByCid(QueryCondition queryCondition, Long cid);

    AttrGroupVo queryArrtGroupById(Long gid);

    List<AttrGroupVo> queryAttrByCid(Long catId);
}

