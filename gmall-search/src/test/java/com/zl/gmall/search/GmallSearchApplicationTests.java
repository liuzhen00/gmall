package com.zl.gmall.search;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.search.feign.GmallPmsFeign;
import com.zl.gmall.search.feign.GmallWmsFeign;
import com.zl.gmall.search.repository.GoodsRepository;
import com.zl.gmall.search.vo.GoodsVo;
import com.zl.gmall.search.vo.SearchAttrValue;
import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.feign.GmallWmsApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GmallWmsFeign gmallWmsFeign;

    @Autowired
    private GmallPmsFeign gmallPmsFeign;




    @Test
    void contextLoads() {
        this.restTemplate.createIndex(GoodsVo.class);
        this.restTemplate.putMapping(GoodsVo.class);
    }

    @Test         //数据导入
    void importData(){
        Long pageNum=1l;
        Long pageSize=100l;
        do {
            //1、分页查询SPU
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setPage(pageNum);
            queryCondition.setLimit(pageSize);
            Resp<List<SpuInfoEntity>> listResp = this.gmallPmsFeign.querySpusByPage(queryCondition);
            List<SpuInfoEntity> spuInfoEntities = listResp.getData();
            //一个SPU下面有多个SKU,遍历SPU根据SPUID查询SKU的参数
            spuInfoEntities.forEach(spuInfoEntity -> {
                Resp<List<SkuInfoEntity>> skuInfoResp = this.gmallPmsFeign.querySkuInfo(spuInfoEntity.getId());
                List<SkuInfoEntity> skuInfoEntities = skuInfoResp.getData();
                //判断SKU信息是否为空 如果不为空 就把SKU信息转换成为Goods
                if (!CollectionUtils.isEmpty(skuInfoEntities)) {
                    List<GoodsVo> goodsVos = skuInfoEntities.stream().map(skuInfoEntity -> {
                        GoodsVo goodsVo = new GoodsVo();
                        //根据SKUID查询属性以及值
                        Resp<List<ProductAttrValueEntity>> searchAttrValueSpuId = this.gmallPmsFeign.querySearchAttrValueSpuId(spuInfoEntity.getId());
                        List<ProductAttrValueEntity> productAttrValueEntities = searchAttrValueSpuId.getData();
                        if (!CollectionUtils.isEmpty(productAttrValueEntities)) {
                            //参数值不为空就把属性值复制到AttrValue中
                            List<SearchAttrValue> searchAttrValues = productAttrValueEntities.stream().map(productAttrValueEntity -> {
                                SearchAttrValue searchAttrValue = new SearchAttrValue();
                                searchAttrValue.setAttrId(productAttrValueEntity.getAttrId());
                                searchAttrValue.setAttrName(productAttrValueEntity.getAttrName());
                                searchAttrValue.setAttrValue(productAttrValueEntity.getAttrValue());
                                return searchAttrValue;
                            }).collect(Collectors.toList());
                            goodsVo.setAttrs(searchAttrValues);
                        }
                        //查询品牌
                        Resp<BrandEntity> brandEntityResp = this.gmallPmsFeign.queryByBrandId(skuInfoEntity.getBrandId());
                        BrandEntity brandEntity = brandEntityResp.getData();
                        if (brandEntity != null) {
                            goodsVo.setBrandId(skuInfoEntity.getBrandId());
                            goodsVo.setBrandName(brandEntity.getName());
                        }

                        //查询分类
                        Resp<CategoryEntity> categoryEntityResp = this.gmallPmsFeign.info(skuInfoEntity.getCatalogId());
                        CategoryEntity categoryEntity = categoryEntityResp.getData();
                        if (categoryEntity != null) {
                            goodsVo.setCategoryId(skuInfoEntity.getCatalogId());
                            goodsVo.setCategoryName(categoryEntity.getName());
                        }
                        //查询SKU的库存信息
                        Resp<List<WareSkuEntity>> wareSkuResp = this.gmallWmsFeign.queryWareSku(skuInfoEntity.getSkuId());
                        List<WareSkuEntity> wareSkuEntities = wareSkuResp.getData();
                        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                            boolean flag = wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
                            goodsVo.setStore(flag);
                        }
                        goodsVo.setTitle(skuInfoEntity.getSkuTitle());
                        goodsVo.setSkuId(skuInfoEntity.getSkuId());
                        goodsVo.setCreateTime(spuInfoEntity.getCreateTime());
                        goodsVo.setPic(skuInfoEntity.getSkuDefaultImg());
                        goodsVo.setPrice( skuInfoEntity.getPrice().doubleValue());
                        goodsVo.setSale(null);
                        return goodsVo;
                    }).collect(Collectors.toList());
                    this.goodsRepository.saveAll(goodsVos);
                }

            });
            pageSize = (long) spuInfoEntities.size();
            pageNum++;
        }while (pageSize==100);

    }

}
