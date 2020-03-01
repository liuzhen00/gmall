package com.zl.gmall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-29 8:51
 */
@Data
public class SearchResponseVo {

    //品牌
    private SearchResponseAttrVo brand;

    //分类
    private SearchResponseAttrVo category;

    private List<SearchResponseAttrVo> attrs;

    private Long total;  //总记录数

    private Integer pageNum;

    private Integer pageSize;

    private List<GoodsVo> data;
}
