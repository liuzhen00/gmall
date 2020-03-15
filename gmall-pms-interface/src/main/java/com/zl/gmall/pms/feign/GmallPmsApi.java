package com.zl.gmall.pms.feign;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.pms.vo.CategoryVo;
import com.zl.gmall.pms.vo.ItemGroupVo;
import io.swagger.annotations.ApiOperation;
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

    //2.根据分类id查询商品分类
    @GetMapping("pms/category/info/{catId}")
    public Resp<CategoryEntity> info(@PathVariable("catId") Long catId);

    //3.根据品牌id查询品牌
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


    //1.根据skuId查询sku信息
    @GetMapping("pms/skuinfo/info/{skuId}")
    public Resp<SkuInfoEntity> querySkuById(@PathVariable("skuId") Long skuId);

    //4.根据spuId查询spu信息
    @GetMapping("pms/spuinfo/info/{id}")
    public Resp<SpuInfoEntity> querySpuById(@PathVariable("id") Long id);

    //7.根据spuId查询SkuSaleAttrValueEntity属性
    @GetMapping("pms/skusaleattrvalue/{spuId}")
    public Resp<List<SkuSaleAttrValueEntity>> querySkuSaleBySpuId(@PathVariable("spuId")Long spuId);


    //8.根据SKUID查询sku图片信息
    @GetMapping("pms/skuimages/{skuId}")
    public Resp<List<SkuImagesEntity>> querySkuImageById(@PathVariable("skuId") Long skuId);


    //9.根据SpuId查询spu图片信息

    @GetMapping("pms/spuinfodesc/info/{spuId}")
    public Resp<SpuInfoDescEntity> querySpuDescById(@PathVariable("spuId") Long spuId);

    //根据分类ID查询规格参数组
    @GetMapping("pms/attrgroup/attrGroupVallue")
    public Resp<List<ItemGroupVo>> queryItemGroupVoById(@RequestParam("spuId")Long spuId,
                                                        @RequestParam("cid")Long cid);

    @GetMapping("pms/skusaleattrvalue/sku/{skuId}")
    public Resp<List<SkuSaleAttrValueEntity>> querySkuSaleById(@PathVariable("skuId")Long skuId);





}
