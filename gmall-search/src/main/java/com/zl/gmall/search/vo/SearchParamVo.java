package com.zl.gmall.search.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2020-02-28 22:36
 */
@Data
public class SearchParamVo {

    private String keyword;  //关键词

    private Long[] brandId;  //品牌ID

    private Long[] categoryId;  //分类ID

    private String[] props;   //规格参数的过滤条件

    private String order;   //排序 冒号前是排序字段

    private Boolean store; //是否有货

    //价格区间

    private Double priceFrom;
    private Double priceTo;

    //分页参数
    private Integer pageNum=1;
    private final Integer pageSize=20;


}
