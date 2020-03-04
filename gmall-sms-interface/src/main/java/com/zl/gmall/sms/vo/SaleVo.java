package com.zl.gmall.sms.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2020-03-03 15:30
 */
@Data
public class SaleVo {

    //促销属性

    private String type; //促销类型 0-优惠券 1-满减 2-阶梯

    private String desc;  //促销信息的名称
}
