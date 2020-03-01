package com.zl.gmall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-29 8:56
 */
@Data
public class SearchResponseAttrVo {

    private Long attrId;

    private String attrName;

    private List<String> attrValues;
}
