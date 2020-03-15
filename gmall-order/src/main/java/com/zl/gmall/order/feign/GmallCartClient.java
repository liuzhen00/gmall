package com.zl.gmall.order.feign;

import com.zl.gmall.cart.feign.GmallCartApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2020-03-10 18:27
 */
@FeignClient("cart-service")
public interface GmallCartClient extends GmallCartApi {
}
