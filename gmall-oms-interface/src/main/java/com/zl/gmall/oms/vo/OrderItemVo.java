package com.zl.gmall.oms.vo;

import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.zl.gmall.sms.vo.SaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-10 12:55
 */
@Data
public class OrderItemVo {

    private Long skuId;
    private String title;
    private String defaultImage;
    private BigDecimal price;
    private Integer count;

    private BigDecimal weight;

    private Boolean store;
    //销售属性
    private List<SkuSaleAttrValueEntity> skuAttrValue;

    private List<SaleVo> saleVo;  //营销属性

}
