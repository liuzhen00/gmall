package com.zl.gmall.cart.entity;

import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.zl.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-09 12:23
 */
@Data
public class Cart {

    private Long skuId;

    private String title;

    private String defaultImage;

    private BigDecimal price;   //加入购物车的价格

    private Integer count;  //购买时的数量

    private BigDecimal currentPrice;

    private List<SkuSaleAttrValueEntity> saleAttrValue; //商品的规格参数

    private List<SaleVo> saleVO;  //营销信息

    private boolean check;

}
