package com.zl.gmall.cart.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.cart.entity.Cart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-10 18:01
 */
public interface GmallCartApi {

    @GetMapping("cart/check/{userId}")
    public Resp<List<Cart>> queryCheckCarts(@PathVariable("userId")Long userId);

}
