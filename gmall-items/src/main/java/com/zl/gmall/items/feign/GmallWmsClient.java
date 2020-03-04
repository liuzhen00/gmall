package com.zl.gmall.items.feign;

import com.zl.gmall.sms.feign.GmallSmsApi;
import com.zl.gmall.wms.feign.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-03 22:38
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
