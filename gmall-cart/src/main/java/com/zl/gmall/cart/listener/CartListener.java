package com.zl.gmall.cart.listener;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.cart.feign.GmallPmsClient;
import com.zl.gmall.pms.entity.SkuInfoEntity;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-03-09 21:43
 */
@Component
public class CartListener {

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PRICE_PREFIX="cart:price:";

    private static final String KEY_PREFIX="cart:";

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value="CART-PRICE-QUEUE",durable = "true"),
            exchange = @Exchange(value="PMS-SPU-EXCHANGE",ignoreDeclarationExceptions = "true",type= ExchangeTypes.TOPIC),
            key = {"item.update"}
    ))
    public void listener(Long spuId){
        Resp<List<SkuInfoEntity>> listResp = this.gmallPmsClient.querySkuInfo(spuId);
        List<SkuInfoEntity> skuInfoEntities = listResp.getData();
        if(CollectionUtils.isEmpty(skuInfoEntities)){
            return;
        }
        skuInfoEntities.forEach(skuInfoEntity -> this.redisTemplate.opsForValue().set(PRICE_PREFIX+skuInfoEntity.getSkuId(),skuInfoEntity.getPrice().toString()));
    }

    @RabbitListener(bindings = @QueueBinding(
           value=@Queue(value="ORDER-CART-QUEUE",durable = "true"),
            exchange = @Exchange(value="ORDER-EXCHANGE",ignoreDeclarationExceptions = "true",type=ExchangeTypes.TOPIC),
            key={"cart.delete"}
    ))
    public void deleteCart(Map<String,Object> map){
        if(CollectionUtils.isEmpty(map)){
            return;
        }
        Long userId =(Long) map.get("userId");
        String skuIdsStr = map.get("skuIds").toString();
        if(StringUtils.isEmpty(skuIdsStr)){
            return;
        }
        List<String> skuIds = JSON.parseArray(skuIdsStr, String.class);
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(KEY_PREFIX + userId);
        hashOps.delete(skuIds.toArray());

    }

}
