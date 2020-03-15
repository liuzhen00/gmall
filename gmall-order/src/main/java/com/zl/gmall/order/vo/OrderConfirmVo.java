package com.zl.gmall.order.vo;

import com.zl.gmall.oms.vo.OrderItemVo;
import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-10 12:38
 */
@Data
public class OrderConfirmVo {

    //收货地址
    private List<MemberReceiveAddressEntity> addresses;

    //购物清单
    private List<OrderItemVo> orderItems;

    //可用积分
    private Integer bounds;

    //订单令牌，防止重复提交
    private String orderToken;






}
