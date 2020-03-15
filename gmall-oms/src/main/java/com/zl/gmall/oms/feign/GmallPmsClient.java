package com.zl.gmall.oms.feign;

import com.zl.gmall.pms.feign.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-11 23:04
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
