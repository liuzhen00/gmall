package com.zl.gmall.cart.feign;

import com.zl.gmall.pms.feign.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-09 18:29
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
