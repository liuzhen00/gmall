package com.zl.gmall.cart.service;

import com.alibaba.fastjson.JSON;
import com.zl.gmall.cart.entity.Cart;
import com.zl.gmall.cart.entity.UserInfo;
import com.zl.gmall.cart.feign.GmallPmsClient;
import com.zl.gmall.cart.feign.GmallSmsClient;
import com.zl.gmall.cart.interceptor.LoginInterceptor;
import com.zl.gmall.pms.entity.SkuInfoEntity;
import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.zl.gmall.sms.vo.SaleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author shkstart
 * @create 2020-03-09 18:33
 */
@Service
public class CartService {

    private static final String CART_PREFIX="cart:";

    private static final String PRICE_PREFIX="cart:price:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private GmallSmsClient gmallSmsClient;

    public void addCart(Cart cart) {
      //判断用户的登录状态,如果是已经登录的用户,则需要使用用户ID去存储,如果没有登录则需要使用UUID
        String key = generatedKey();
        //获取Redis中的购物车
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        String skuId = cart.getSkuId().toString();
        if(hashOps.hasKey(skuId)){
            //如果包含该ID则添加数量
            String cartJson = hashOps.get(skuId).toString();  //返回的是一个JSON字符串
            //将JSON转为Cart对象
            Integer count = cart.getCount();
            cart = parseObject(cartJson, Cart.class);
            cart.setCount(cart.getCount()+count);

        }else{
            //如果不包含，则直接新增
            SkuInfoEntity skuInfoEntity = this.gmallPmsClient.querySkuById(cart.getSkuId()).getData();
            if(skuInfoEntity!=null){
                cart.setTitle(skuInfoEntity.getSkuTitle());
                cart.setPrice(skuInfoEntity.getPrice());
                cart.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
                //查询sku的销售属性
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = this.gmallPmsClient.querySkuSaleById(cart.getSkuId()).getData();
                cart.setSaleAttrValue(skuSaleAttrValueEntityList);
                //查询销售属性
                List<SaleVo> saleVos = this.gmallSmsClient.querySkuBySaleInfo(cart.getSkuId()).getData();
                cart.setSaleVO(saleVos);
                cart.setCheck(true);

                this.redisTemplate.opsForValue().set(PRICE_PREFIX+skuId,skuInfoEntity.getPrice().toString());
            }

        }

        hashOps.put(skuId,JSON.toJSONString(cart));

    }

    //查询购物车



    private String generatedKey() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key=CART_PREFIX;
        if(userInfo.getId()==null){
            //说明没有登录
            key+=userInfo.getUserKey();
        }else{
            key+=userInfo.getId();
        }
        return key;
    }

    //查询购物车
    public List<Cart> queryCarts() {
        //判断是否已经登录
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //首先查询userInfo未登录的购物车信息
        String unKey=CART_PREFIX+userInfo.getUserKey();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(unKey);
        //获取userKey所有的key
        List<Object> cartJsons = hashOps.values();
        List<Cart> unCart=null;
        //当集合不为空时:
        if(!CollectionUtils.isEmpty(cartJsons)){
            //将集合转换成为Cart对象
            unCart = cartJsons.stream().map(cartJson -> {
                Cart cart = parseObject(cartJson.toString(), Cart.class);
                //从缓存中查询最新的价格
                String price = this.redisTemplate.opsForValue().get(PRICE_PREFIX + cart.getSkuId());
                cart.setCurrentPrice(new BigDecimal(price));
                return cart;
            }).collect(Collectors.toList());
        }

        //判断是否登录，没有登录直接返回
        Long userId = userInfo.getId();
        if(userId==null){
            return unCart;
        }
        //如果已经登录，合并购物车
        BoundHashOperations<String, Object, Object> loginOps = this.redisTemplate.boundHashOps(CART_PREFIX + userId);
        if(!CollectionUtils.isEmpty(unCart)){
            //判断这个已经登录的购物车在这个已经登录的购物车中是否存在
            unCart.forEach(cart -> {

                if(loginOps.hasKey(cart.getSkuId().toString())){
                    //修改数量
                    String json = loginOps.get(cart.getSkuId()).toString();
                    Integer count = cart.getCount();
                    cart = parseObject(json, Cart.class);
                    cart.setCount(cart.getCount()+count);
                }
                //添加
                loginOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
            });
        }

        //删除未登录的购物车
      this.redisTemplate.delete(unKey);

        //将loginOps转成为ListCart
        List<Object> loginCartJson = loginOps.values();
        if(!CollectionUtils.isEmpty(loginCartJson)){
        return loginCartJson.stream().map(cartJson->{
            //从缓存中查询最新的价格
            Cart cart=JSON.parseObject(cartJson.toString(), Cart.class);
            String price = this.redisTemplate.opsForValue().get(PRICE_PREFIX + cart.getSkuId());
            cart.setCurrentPrice(new BigDecimal(price));
            return cart;
        }).collect(Collectors.toList());

        }
        return null;

    }
    //修改购物车数量
    public void updateCart(Cart cart) {
        String key = generatedKey();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        String skuId = cart.getSkuId().toString();
        if(hashOps.hasKey(cart.getSkuId().toString())){
            String json = hashOps.get(skuId).toString();
            Integer count = cart.getCount();
            cart = parseObject(json, Cart.class);
            cart.setCount(count);
            hashOps.put(skuId,JSON.toJSONString(cart));
        }
    }

    public void updateCheck(Cart cart) {
        String key = generatedKey();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        String skuId = cart.getSkuId().toString();
        if(hashOps.hasKey(cart.getSkuId().toString())){
            String json = hashOps.get(skuId).toString();
            boolean check = cart.isCheck();
            cart = parseObject(json, Cart.class);
            cart.setCheck(check);
            hashOps.put(skuId,JSON.toJSONString(cart));
        }

    }

    //删除成功
    public void deleteCart(Long skuId) {
        String key = generatedKey();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());

    }

    //购物车List
    public List<Cart> queryCheckCarts(Long userId) {
        String key=CART_PREFIX+userId;
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> jsonCartList = hashOps.values();
        if(!CollectionUtils.isEmpty(jsonCartList)){
           return jsonCartList.stream().map(jsonCart->JSON.parseObject(jsonCart.toString(),Cart.class)).filter(cart->cart.isCheck()).collect(Collectors.toList());
        }
         return null;
    }
}
