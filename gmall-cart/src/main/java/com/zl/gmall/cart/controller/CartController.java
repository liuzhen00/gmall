package com.zl.gmall.cart.controller;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.cart.entity.Cart;
import com.zl.gmall.cart.entity.UserInfo;
import com.zl.gmall.cart.interceptor.LoginInterceptor;
import com.zl.gmall.cart.service.CartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-09 12:13
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

   @PostMapping("/add")
   public Resp<Object> addCart(@RequestBody Cart cart){
       this.cartService.addCart(cart);
       return Resp.ok("添加成功");
   }

   //查询购物车

    @GetMapping
    public Resp<List<Cart>> queryCarts(){
        List<Cart> cartList=cartService.queryCarts();
        return Resp.ok(cartList);
    }

    //修改购物车数量
    @PostMapping("updateNum")
    public Resp<Object> updateCart(@RequestBody Cart cart){
       this.cartService.updateCart(cart);
       return Resp.ok("修改成功");

    }

    //修改购物车的选中

    //修改购物车数量
    @PostMapping("/updateCheck")
    public Resp<Object> updateCheck(@RequestBody Cart cart){
        this.cartService.updateCheck(cart);
        return Resp.ok("修改成功");

    }

    //删除购物车商品

    @DeleteMapping("{skuId}")
    public Resp<Object> deleteCart(@PathVariable("skuId")Long skuId){
       this.cartService.deleteCart(skuId);
       return Resp.ok("删除成功");
    }

    //查询购物车中被选中的购吴条目
    @GetMapping("check/{userId}")
    public Resp<List<Cart>> queryCheckCarts(@PathVariable("userId")Long userId){
       List<Cart> cartList=this.cartService.queryCheckCarts(userId);

       return Resp.ok(cartList);
    }

}
