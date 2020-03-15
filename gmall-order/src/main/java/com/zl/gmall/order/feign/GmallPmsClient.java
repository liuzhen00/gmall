package com.zl.gmall.order.feign;

import com.zl.gmall.pms.feign.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-10 13:21
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
