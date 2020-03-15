package com.zl.gmall.order.feign;

import com.zl.gmall.wms.feign.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-10 13:24
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
