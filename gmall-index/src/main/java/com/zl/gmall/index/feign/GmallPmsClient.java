package com.zl.gmall.index.feign;

import com.zl.gmall.pms.feign.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-01 12:08
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
