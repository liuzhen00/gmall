package com.zl.gmall.oms.vo;

import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-11 14:09
 */
@Data
public class OrderSubmitVo {
    //提交订单

    //防止重复提交
    private String orderToken;

    //总价格
    private BigDecimal totalPrice;

    //收货地址
    private MemberReceiveAddressEntity address;

    //支付方式
    private Integer payType;

    //配送方式

    private String deliveryCompany;

    //订单详情
    private List<OrderItemVo> ordersItem;

    //积分信息
    private Integer bounds;

    private Long userId;

}
