package com.zl.gmall.search.feign;

import com.zl.gmall.wms.feign.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-02-27 11:55
 */
@FeignClient("wms-service")
public interface GmallWmsFeign extends GmallWmsApi {
}
