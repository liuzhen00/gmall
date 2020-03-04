package com.zl.gmall.pms.controller;

import java.util.Arrays;
import java.util.List;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.vo.AttrGroupVo;
import com.zl.gmall.pms.vo.ItemGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.zl.gmall.pms.entity.AttrGroupEntity;
import com.zl.gmall.pms.service.AttrGroupService;




/**
 * 属性分组
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:52:16
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 查询分类的下的以及规格参数
     */

    @GetMapping("/attrGroupVallue")
    public Resp<List<ItemGroupVo>> queryItemGroupVoById(@RequestParam("spuId")Long spuId,
                                                         @RequestParam("cid")Long cid){
        List<ItemGroupVo> itemGroupVoList= attrGroupService.queryItemGroupVoById(spuId,cid);
        return Resp.ok(itemGroupVoList);

    }

    @GetMapping("withattr/{gid}")
    public Resp<AttrGroupVo> queryArrtGroupById(@PathVariable("gid")Long gid){
        AttrGroupVo attrGroupVo =  attrGroupService.queryArrtGroupById(gid);
        return Resp.ok(attrGroupVo);
    }

    /**
     * 查询分类的分组：
     * @param queryCondition
     * @param cid
     * @return
     */
    @ApiOperation("分页查询分组信息")
    @GetMapping("{cid}")
    public Resp<PageVo> queryGroupByCid(QueryCondition queryCondition,@PathVariable("cid")Long cid){
       PageVo pageVo=attrGroupService.queryGroupByCid(queryCondition,cid);
       return Resp.ok(pageVo);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attrgroup:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrGroupService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrGroupId}")
    @PreAuthorize("hasAuthority('pms:attrgroup:info')")
    public Resp<AttrGroupEntity> info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return Resp.ok(attrGroup);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attrgroup:save')")
    public Resp<Object> save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attrgroup:update')")
    public Resp<Object> update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attrgroup:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return Resp.ok(null);
    }

}
