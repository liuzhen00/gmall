package com.zl.gmall.order.feign;

import com.zl.gmall.sms.feign.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-10 13:22
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
