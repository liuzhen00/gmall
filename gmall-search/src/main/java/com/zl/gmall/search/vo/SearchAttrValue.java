package com.zl.gmall.search.vo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author shkstart
 * @create 2020-02-27 10:03
 */
@Data      //规格参数
public class SearchAttrValue {

    @Field(type = FieldType.Long)
    private Long attrId;

    @Field(type = FieldType.Keyword)
    private String attrName;

    @Field(type = FieldType.Keyword)
    private String attrValue;
}
