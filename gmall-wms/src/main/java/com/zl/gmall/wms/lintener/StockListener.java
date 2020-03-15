package com.zl.gmall.wms.lintener;

import com.alibaba.fastjson.JSON;
import com.zl.gmall.wms.dao.WareSkuDao;
import com.zl.gmall.wms.vo.SkuLockVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-12 0:45
 */
@Component
public class StockListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private static final String KEY_PREFIX="wms:lock:";

    @Autowired
    private WareSkuDao wareSkuDao;

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value = "WMS-UNLOCK-QUEUE",durable = "true"),
            exchange = @Exchange(value="ORDER-EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key={"wms.unlock"}

    ))
    public void unlockListener(String oderToken){
        String lockjson = this.redisTemplate.opsForValue().get(KEY_PREFIX + oderToken);
       if(StringUtils.isEmpty(lockjson)){
           return ;
       }
        List<SkuLockVo> skuLockVos = JSON.parseArray(lockjson, SkuLockVo.class);
       //解库存
        skuLockVos.forEach(skuLockVo->{
            this.wareSkuDao.unlockStock(skuLockVo.getWareSkuId(),skuLockVo.getCount());
        });
    }

    //减去库存
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value="WMS-MINUS-QUEUE",durable = "true"),
            exchange = @Exchange(value="ORDER-EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"stock.minus"}
    ))
    public void minusLockListener(String orderToken){

        //获取Redis中库存信息
        String lockJson = this.redisTemplate.opsForValue().get(KEY_PREFIX + orderToken);
        System.out.println("lockjson:"+lockJson);
        if(StringUtils.isEmpty(lockJson)){
            return;
        }
        List<SkuLockVo> lockVos = JSON.parseArray(lockJson, SkuLockVo.class);
        //解锁
        lockVos.forEach(skuLockVo -> {
            this.wareSkuDao.minusStock(skuLockVo.getWareSkuId(),skuLockVo.getCount());
        });
        //删除库存信息
        this.redisTemplate.delete(KEY_PREFIX+orderToken);
    }


}
