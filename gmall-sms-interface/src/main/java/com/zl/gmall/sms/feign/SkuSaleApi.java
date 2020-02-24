package com.zl.gmall.sms.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.sms.vo.SkuSaleDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shkstart
 * @create 2020-02-23 13:45
 */

public interface SkuSaleApi {
    @PostMapping("/sms/skubounds/skusale/save")
    public Resp<Object> saveSkuSaleInfo(@RequestBody SkuSaleDTO saleDTO);
}
