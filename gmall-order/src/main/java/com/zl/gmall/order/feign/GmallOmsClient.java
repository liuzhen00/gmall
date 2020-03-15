package com.zl.gmall.order.feign;

import com.zl.gmall.oms.feign.GmallOmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-11 22:28
 */
@FeignClient("oms-service")
public interface GmallOmsClient extends GmallOmsApi {
}
