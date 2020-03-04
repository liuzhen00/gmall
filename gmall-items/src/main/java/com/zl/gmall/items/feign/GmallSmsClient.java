package com.zl.gmall.items.feign;

import com.zl.gmall.pms.feign.GmallPmsApi;
import com.zl.gmall.sms.feign.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-03 22:38
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
