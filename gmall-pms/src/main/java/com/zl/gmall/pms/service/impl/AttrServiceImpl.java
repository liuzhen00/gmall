package com.zl.gmall.pms.service.impl;

import com.zl.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.zl.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.zl.gmall.pms.service.AttrAttrgroupRelationService;
import com.zl.gmall.pms.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.pms.dao.AttrDao;
import com.zl.gmall.pms.entity.AttrEntity;
import com.zl.gmall.pms.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrgroupRelationDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryAttrByIdAndType(QueryCondition queryCondition,
                                       Integer type, Long cid) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id",cid);
        if(type!=null){
            wrapper.eq("attr_type",type);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(queryCondition),wrapper

        );

        return new PageVo(page);
    }

    @Override
    public void saveGroupAndAttr(AttrVo attrVo) {
        //先插入属性表
        this.save(attrVo);
        Long attrId = attrVo.getAttrId();
        //再插入中间表
        AttrAttrgroupRelationEntity relationEntity=new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrId);
        attrgroupRelationDao.insert(relationEntity);


    }

}