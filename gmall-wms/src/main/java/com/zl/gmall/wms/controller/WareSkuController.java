package com.zl.gmall.wms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zl.gmall.wms.vo.SkuLockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.service.WareSkuService;




/**
 * 商品库存
 *
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:36:37
 */
@Api(tags = "商品库存 管理")
@RestController
@RequestMapping("wms/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    //检验库存并锁住库存
    @PostMapping("check/lock")
    public Resp<List<SkuLockVo>> checkAndCheck(@RequestBody List<SkuLockVo> skuLockVos){
        List<SkuLockVo> skuLockVoList= this.wareSkuService.checkAndCheck(skuLockVos);
        return Resp.ok(skuLockVoList);
    }

    /**
     * 对SKU的库存维护
     * SKU和仓库有一种中间表
     * 中间表关联的仓库的地址
     */

    @GetMapping("{skuId}")
    public Resp<List<WareSkuEntity>> queryWareSku(@PathVariable("skuId")Long skuId){
        List<WareSkuEntity> list = wareSkuService.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId));
        return  Resp.ok(list);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('wms:waresku:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = wareSkuService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('wms:waresku:info')")
    public Resp<WareSkuEntity> info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return Resp.ok(wareSku);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('wms:waresku:save')")
    public Resp<Object> save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('wms:waresku:update')")
    public Resp<Object> update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('wms:waresku:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
