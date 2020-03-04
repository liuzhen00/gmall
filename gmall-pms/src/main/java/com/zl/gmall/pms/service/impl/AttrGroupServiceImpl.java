package com.zl.gmall.pms.service.impl;

import com.zl.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.zl.gmall.pms.dao.AttrDao;
import com.zl.gmall.pms.dao.ProductAttrValueDao;
import com.zl.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.zl.gmall.pms.entity.AttrEntity;
import com.zl.gmall.pms.entity.ProductAttrValueEntity;
import com.zl.gmall.pms.vo.AttrGroupVo;
import com.zl.gmall.pms.vo.ItemGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.pms.dao.AttrGroupDao;
import com.zl.gmall.pms.entity.AttrGroupEntity;
import com.zl.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    //中间表
    @Autowired
    private AttrAttrgroupRelationDao attrgroupRelationDao;
    //属性表
    @Autowired
    private AttrDao attrDao;

    @Autowired
    private ProductAttrValueDao attrValueDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryGroupByCid(QueryCondition queryCondition, Long cid) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(queryCondition),
                new QueryWrapper<AttrGroupEntity>().eq("catelog_id",cid)
        );

        return new PageVo(page);
    }

    @Override
    public AttrGroupVo queryArrtGroupById(Long gid) {
        /**
         * 最后显示的数据：
         *  属性的ID
         *  属性的分组名称
         *  属性的参数值
         *  需要用到的表：
         *   主页显示：分组表
         *   点击分组信息：查出该组的详情信息，但是必须拷中间表来维护
         */
        AttrGroupVo groupVo = new AttrGroupVo();
        //1.查询出该分组的ID信息
        AttrGroupEntity attrGroupEntity = getById(gid);
        BeanUtils.copyProperties(attrGroupEntity,groupVo);
        //2.使用该分组ID查询出该组下的所有属性ID
        QueryWrapper<AttrAttrgroupRelationEntity> wrapperRelation = new QueryWrapper<>();
        wrapperRelation.eq("attr_group_id",gid);
        List<AttrAttrgroupRelationEntity> relationEntityList = attrgroupRelationDao.selectList(wrapperRelation);
        groupVo.setRelations(relationEntityList);
        //根据获取的属性ID，查询出该分组的所有属性
        //a、判断该集合是否为空
        if(CollectionUtils.isEmpty(relationEntityList)){
            return groupVo;
        }
        //b、过滤该集合中所有attrId
        //将一个集合转为另一个集合的，然后过滤集合中的属性
        List<Long> attrIdList = relationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        //c、查询出该ID下的所有属性
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIdList);
        groupVo.setAttrEntities(attrEntities);
        return groupVo;
    }

    @Override
    public List<AttrGroupVo>  queryAttrByCid(Long catId) {


       //按照分类ID查询分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        System.out.println(attrGroupEntities);
        if(CollectionUtils.isEmpty(attrGroupEntities)){
            return null;
        }
        //查询每一组下的规格参数
        // 查询出每组下的规格参数
        List<AttrGroupVo> attrGroupVOs = attrGroupEntities.stream().map(attrGroupEntity -> {
            return this.queryArrtGroupById(attrGroupEntity.getAttrGroupId());
        }).collect(Collectors.toList());

        return attrGroupVOs;

    }

    @Override
    public List<ItemGroupVo> queryItemGroupVoById(Long spuId,Long cid) {

        //根据分类查分组
        List<AttrGroupEntity> groupEntityList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));
        if(CollectionUtils.isEmpty(groupEntityList)){
            return null;
        }
        //分组遍历每个组下的Attr
    return  groupEntityList.stream().map(group ->{
            ItemGroupVo groupVo = new ItemGroupVo();
            groupVo.setId(group.getAttrGroupId());
            groupVo.setName(group.getAttrGroupName());
            List<AttrAttrgroupRelationEntity> relationEntities = this.attrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", group.getAttrGroupId()));
            //根据关系表去查询attrId,得到规格参数的分组
            if(!CollectionUtils.isEmpty(relationEntities)){
                List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
                List<ProductAttrValueEntity> productAttrValueEntities = this.attrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().in("attr_id", attrIds));
                groupVo.setAttrs(productAttrValueEntities);
            }
           return groupVo;


        }).collect(Collectors.toList());




    }

}