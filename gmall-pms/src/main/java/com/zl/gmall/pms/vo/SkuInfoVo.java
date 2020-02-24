package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.SkuInfoEntity;
import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-23 11:02
 */
@Data
public class SkuInfoVo extends SkuInfoEntity {
    //pms_sku_images
    private List<String> images;

    //积分活动 sms_sku_bounds
    private BigDecimal growBounds;  //成长积分
    private BigDecimal buyBounds;  //购物积分
    private List<Integer> work;  //积分生效情况

    //满减活动  sms_sku_full_reduction
    private BigDecimal fullPrice;   //满多少
    private BigDecimal reducePrice;   //减多少
    private Integer fullAddOther;

    //sms_sku_ladder
    private Integer fullCount;        //满几折
    private BigDecimal discount;      //打几折

    /**
     * 是否叠加其它优惠
     */
    private Integer addOther;    //是否叠加使用其它优惠券
    private List<SkuSaleAttrValueEntity> saleAttrs;
}
