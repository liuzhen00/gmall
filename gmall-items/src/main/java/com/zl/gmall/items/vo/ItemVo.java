package com.zl.gmall.items.vo;

import com.zl.gmall.pms.entity.SkuImagesEntity;
import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.zl.gmall.pms.vo.ItemGroupVo;
import com.zl.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-03 15:06
 */
@Data
public class ItemVo {

    //分类
    private Long categoryId;
    private String categoryName;


    //品牌
    private Long brandId;
    private String brandName;

    //关于SKU的信息

    private Long skuId;
    private Long spuId;
    private String spuName;
    private String skuTitle;   //标题
    private String skuSubTitle;
    private BigDecimal weight;  //重量
    private BigDecimal price;   //价格

    //SKU的图片
    private List<SkuImagesEntity> images;

    //sku的所有促销属性
    private List<SaleVo> sales;

    //是否有货
    private Boolean store=false;

    //所有销售信息的属性sku
    private List<SkuSaleAttrValueEntity> saleAttrs;

    //spu所有的基本属性
    private List<ItemGroupVo> attrGroups;

    //海报信息spuInfo下的图片信息
    private List<String> desc;


}
