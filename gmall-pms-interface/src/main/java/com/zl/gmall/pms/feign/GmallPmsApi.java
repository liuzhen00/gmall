package com.zl.gmall.pms.feign;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.pms.vo.CategoryVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-27 11:18
 */

public interface GmallPmsApi {
    //分页查询已上架的SPU信息
    @PostMapping("pms/spuinfo/page")
    public Resp<List<SpuInfoEntity>> querySpusByPage(@RequestBody QueryCondition queryCondition);

    //根据SpuId查询对应的SKU信息
    @GetMapping("pms/skuinfo/{spuId}")
    public Resp<List<SkuInfoEntity>> querySkuInfo(@PathVariable("spuId") Long spuId);

    //根据分类id查询商品分类
    @GetMapping("pms/category/info/{catId}")
    public Resp<CategoryEntity> info(@PathVariable("catId") Long catId);

    //根据品牌id查询品牌
    @GetMapping("pms/brand/info/{brandId}")
    public Resp<BrandEntity> queryByBrandId(@PathVariable("brandId") Long brandId);


    //根据spuId查询检索规格参数及值
    @GetMapping("pms/productattrvalue/{spuId}")
    public Resp<List<ProductAttrValueEntity>> querySearchAttrValueSpuId(@PathVariable("spuId") Long spuId);

    //根据分类查询
    @GetMapping("pms/category")
    public Resp<List<CategoryEntity>> queryCategoryByLebelorPid(@RequestParam(value="level",defaultValue = "0")Integer level,
                                                                @RequestParam(value="parentCid",required = false)Long pid);

    @GetMapping("pms/category/{pid}")
    public Resp<List<CategoryVo>> queryCategoryByLebe2ByPid(@PathVariable("pid") Long pid);
}
