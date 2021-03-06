package com.zl.gmall.search.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-26 23:50
 */
@Data
@Document(indexName = "goods",type="info",shards = 3,replicas = 2)
public class GoodsVo {

    @Id
    private Long skuId;

    @Field(type = FieldType.Keyword,index = false)
    private String pic;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Double)
    private Double price;

    @Field(type=FieldType.Long)
    private Long sale;//销量

    @Field(type = FieldType.Boolean)
    private Boolean store;   //是否有货

    @Field(type=FieldType.Date)
    private Date createTime;

    @Field(type=FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;
    @Field(type = FieldType.Nested)
    private List<SearchAttrValue> attrs;

}
