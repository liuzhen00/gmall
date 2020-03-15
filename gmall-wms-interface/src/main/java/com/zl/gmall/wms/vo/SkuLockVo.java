package com.zl.gmall.wms.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2020-03-11 15:41
 */
@Data
public class SkuLockVo {

    //锁定的商品ID
    private Long skuId;

    //数量
    private Integer count;

    //锁定的状态
    private Boolean lock;

    //锁定成功时，锁定锁定仓库
    private Long wareSkuId;

    private String orderToken;
}
