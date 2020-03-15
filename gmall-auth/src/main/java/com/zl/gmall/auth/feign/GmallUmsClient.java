package com.zl.gmall.auth.feign;

import com.zl.gmall.ums.feign.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-08 1:24
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}
