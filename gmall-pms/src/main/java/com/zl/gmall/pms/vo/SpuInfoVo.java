package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-23 10:40
 */
@Data
public class SpuInfoVo extends SpuInfoEntity {
    //图片属性
    private List<String> spuImages;

    //基本属性
    private List<ProduceAttrValueVo> baseAttrs;

    //Sku基本信息
    private List<SkuInfoVo> skus;

}
