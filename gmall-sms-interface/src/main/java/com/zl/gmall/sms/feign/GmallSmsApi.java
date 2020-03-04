package com.zl.gmall.sms.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.sms.vo.SaleVo;
import com.zl.gmall.sms.vo.SkuSaleDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-23 13:45
 */

public interface GmallSmsApi {
    @PostMapping("/sms/skubounds/skusale/save")
    public Resp<Object> saveSkuSaleInfo(@RequestBody SkuSaleDTO saleDTO);


    //5.根据skuId查询营销信息
    @GetMapping("sms/skubounds/{skuId}")
    public Resp<List<SaleVo>> querySkuBySaleInfo(@PathVariable("skuId")Long skuId);
}
