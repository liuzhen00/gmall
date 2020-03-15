package com.zl.gmall.oms.feign;

import com.zl.gmall.ums.feign.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-11 22:48
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}
