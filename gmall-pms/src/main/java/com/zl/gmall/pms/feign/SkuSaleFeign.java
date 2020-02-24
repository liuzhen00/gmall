package com.zl.gmall.pms.feign;

import com.zl.gmall.sms.feign.SkuSaleApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-02-23 16:29
 */
@FeignClient("sms-service")
public interface SkuSaleFeign extends SkuSaleApi {
}
