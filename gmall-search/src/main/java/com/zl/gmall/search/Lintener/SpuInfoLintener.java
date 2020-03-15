package com.zl.gmall.search.Lintener;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.search.feign.GmallPmsFeign;
import com.zl.gmall.search.feign.GmallWmsFeign;
import com.zl.gmall.search.repository.GoodsRepository;
import com.zl.gmall.search.vo.GoodsVo;
import com.zl.gmall.search.vo.SearchAttrValue;
import com.zl.gmall.wms.entity.WareSkuEntity;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-03-07 0:51
 */
@Component
public class SpuInfoLintener {
    // 创建索引
    @Autowired
    private GmallPmsFeign gmallPmsFeign;

    @Autowired
    private GmallWmsFeign gmallWmsFeign;


    private GoodsRepository goodsRepository;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gmall.item.create.queue", durable = "true"),
            exchange = @Exchange(
                    value = "gmall.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert"}))
    public void listenCreate(Long id) throws Exception {
        Resp<SpuInfoEntity> spuInfoEntityResp = this.gmallPmsFeign.querySpuById(id);
        SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
        if (spuInfoEntity == null) {
            return;
        }

        Resp<List<SkuInfoEntity>> skuInfoResp = this.gmallPmsFeign.querySkuInfo(id);
        List<SkuInfoEntity> skuInfoEntities = skuInfoResp.getData();
        //判断SKU信息是否为空 如果不为空 就把SKU信息转换成为Goods
        if (!CollectionUtils.isEmpty(skuInfoEntities)) {
            List<GoodsVo> goodsVos = skuInfoEntities.stream().map(skuInfoEntity -> {
                GoodsVo goodsVo = new GoodsVo();
                //根据SKUID查询属性以及值
                Resp<List<ProductAttrValueEntity>> searchAttrValueSpuId = this.gmallPmsFeign.querySearchAttrValueSpuId(id);
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

                goodsVo.setCreateTime(    this.gmallPmsFeign.querySpuById(id).getData().getCreateTime());
                goodsVo.setPic(skuInfoEntity.getSkuDefaultImg());
                goodsVo.setPrice( skuInfoEntity.getPrice().doubleValue());
                goodsVo.setSale(null);
                return goodsVo;
            }).collect(Collectors.toList());
            this.goodsRepository.saveAll(goodsVos);
        }
    }

}
