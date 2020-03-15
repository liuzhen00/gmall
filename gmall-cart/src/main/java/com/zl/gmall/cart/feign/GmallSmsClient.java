package com.zl.gmall.cart.feign;

import com.zl.gmall.sms.feign.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-09 18:30
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
