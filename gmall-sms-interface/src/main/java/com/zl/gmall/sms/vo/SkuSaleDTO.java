package com.zl.gmall.sms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-23 13:15
 */
@Data
public class SkuSaleDTO {

    private Long skuId;

    //积分活动 sms_sku_bounds
    private BigDecimal growBounds;  //成长积分
    private BigDecimal buyBounds;  //购物积分
    private List<Integer> work;  //积分生效情况

    //满减活动  sms_sku_full_reduction
    private BigDecimal fullPrice;   //满多少
    private BigDecimal reducePrice;   //减多少
    private  Integer fullAddOther;

    //sms_sku_ladder
    private Integer fullCount;        //满几折
    private BigDecimal discount;      //打几折

    /**
     * 是否叠加其它优惠
     */
    private Integer addOther;    //是否叠加使用其它优惠券

}
