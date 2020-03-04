package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-03 15:38
 */
@Data
public class ItemGroupVo {
    //基本属性分组以及组下的规格参数
    private Long id;

    private String name; //分组名字

    //一个组可能有多个分组参数
    private List<ProductAttrValueEntity> attrs;
}
