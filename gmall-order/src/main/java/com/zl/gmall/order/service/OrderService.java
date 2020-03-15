package com.zl.gmall.order.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.exception.OrderException;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zl.gmall.cart.entity.Cart;
import com.zl.gmall.oms.entity.OrderEntity;
import com.zl.gmall.order.config.LoginInterceptor;
import com.zl.gmall.oms.vo.OrderItemVo;
import com.zl.gmall.order.vo.OrderConfirmVo;
import com.zl.gmall.order.entity.UserInfo;
import com.zl.gmall.order.feign.*;
import com.zl.gmall.oms.vo.OrderSubmitVo;
import com.zl.gmall.pms.entity.SkuInfoEntity;
import com.zl.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.zl.gmall.sms.vo.SaleVo;
import com.zl.gmall.ums.entity.MemberEntity;
import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.vo.SkuLockVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sound.midi.SoundbankResource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-03-10 13:44
 */
@Service
public class OrderService {

    private static final String ORDER_PRIFIX="order:token:";

    @Autowired
    private GmallCartClient gmallCartClient;

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private GmallWmsClient gmallWmsClient;

    @Autowired
    private GmallSmsClient gmallSmsClient;

    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GmallOmsClient gmallOmsClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    public OrderConfirmVo confirm() {

        OrderConfirmVo confirmVo=new OrderConfirmVo();
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        Long userId = userInfo.getId();
        if(userId==null){
            throw new OrderException("登录信息已经过期");
        }
        //订单的详情列表
        //远程调用购物车,查询购物车中选中的记录
        CompletableFuture<Void> itemVoFuture = CompletableFuture.supplyAsync(() -> {
            List<Cart> cartList = this.gmallCartClient.queryCheckCarts(userId).getData();
            return cartList;
        },threadPoolExecutor).thenAcceptAsync(cartList -> {

                //为了保证数据的实时同步，所以为了查询详情信息需要再次根据SKU查询数据可
                List<OrderItemVo> oderItemVos = cartList.stream().map(cart -> {

                    OrderItemVo oderItem = new OrderItemVo();
                    //根据Sku查询商品的详情信息
                    CompletableFuture<SkuInfoEntity> skuFuture = CompletableFuture.supplyAsync(() -> {

                        SkuInfoEntity skuInfoEntity = this.gmallPmsClient.querySkuById(cart.getSkuId()).getData();

                        System.out.println("skuInfoEntity:"+skuInfoEntity);
                        if (skuInfoEntity != null) {

                            oderItem.setSkuId(skuInfoEntity.getSkuId());
                            oderItem.setTitle(skuInfoEntity.getSkuTitle());
                            oderItem.setCount(cart.getCount());
                            oderItem.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
                            oderItem.setPrice(skuInfoEntity.getPrice());
                            oderItem.setWeight(skuInfoEntity.getWeight());

                        }

                        return skuInfoEntity;
                    },threadPoolExecutor);


                    //查询库存信息
                    CompletableFuture<Void> wareFuture = skuFuture.thenAcceptAsync(skuInfoEntity -> {
                        if (skuInfoEntity != null) {
                            List<WareSkuEntity> wareSkuEntities = this.gmallWmsClient.queryWareSku(cart.getSkuId()).getData();
                            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                                oderItem.setStore(wareSkuEntities.stream().anyMatch(ware -> ware.getStock() > 0));
                            }
                        }
                    },threadPoolExecutor);

                    //查询营销信息
                    CompletableFuture<Void> saleFuture = skuFuture.thenAcceptAsync(skuInfoEntity -> {
                        if (skuInfoEntity != null) {
                            List<SaleVo> saleVoList = this.gmallSmsClient.querySkuBySaleInfo(cart.getSkuId()).getData();
                            if (!CollectionUtils.isEmpty(saleVoList)) {
                                oderItem.setSaleVo(saleVoList);
                            }
                        }
                    },threadPoolExecutor);
                    //查询销售属性
                    CompletableFuture<Void> saleAttrFuture = skuFuture.thenAcceptAsync(skuInfoEntity -> {
                        if (skuInfoEntity != null) {
                            List<SkuSaleAttrValueEntity> saleAttrList = this.gmallPmsClient.querySkuSaleById(cart.getSkuId()).getData();
                            if (!CollectionUtils.isEmpty(saleAttrList)) {
                                oderItem.setSkuAttrValue(saleAttrList);
                            }
                        }
                    },threadPoolExecutor);
                    CompletableFuture.allOf(wareFuture, saleFuture, saleAttrFuture).join();
                    return oderItem;
                }).collect(Collectors.toList());

                confirmVo.setOrderItems(oderItemVos);

        },threadPoolExecutor);


        //查询收货地址
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            List<MemberReceiveAddressEntity> addressList = this.gmallUmsClient.queryAddressByMemberId(userId).getData();
            if (!CollectionUtils.isEmpty(addressList)) {
                confirmVo.setAddresses(addressList);
            }
        },threadPoolExecutor);
        //可用积分
        CompletableFuture<Void> memberFuture = CompletableFuture.runAsync(() -> {
        MemberEntity memberEntity=this.gmallUmsClient.queryMemberById(userId).getData();
        if(memberEntity!=null){
            confirmVo.setBounds(memberEntity.getIntegration());
        }
        },threadPoolExecutor);

        //订单令牌，防止重复提交
        CompletableFuture<Void> tokenFuture = CompletableFuture.runAsync(() -> {
        String orderToken = IdWorker.getTimeId();
        confirmVo.setOrderToken(orderToken);
        this.redisTemplate.opsForValue().set(ORDER_PRIFIX+orderToken,orderToken,3, TimeUnit.HOURS);
        },threadPoolExecutor);

        CompletableFuture.allOf(itemVoFuture,addressFuture,memberFuture,tokenFuture).join();
        return confirmVo;
    }

    public OrderEntity submitOrder(OrderSubmitVo orderSubmitVo) {
        //1.校验是否重复提交,要保证原子性，在高并发情况下，会导致多个线程执行提交同一个订单，导致订单重复提交
        String orderToken = orderSubmitVo.getOrderToken();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        Boolean flag = this.redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(ORDER_PRIFIX + orderToken), orderToken);
        if(!flag){
            throw new OrderException("您多次提交,请稍后重试");
        }
        //2.校验价格
         //计算页面详情的总价格
        List<OrderItemVo> ordersItem = orderSubmitVo.getOrdersItem();
        if(CollectionUtils.isEmpty(ordersItem)){
            throw new OrderException("你没有选中商品,请选择你要购买的商品");
        }
       BigDecimal currentTotalPrice = ordersItem.stream().map(item -> {  //实时总价
            SkuInfoEntity skuInfoEntity = this.gmallPmsClient.querySkuById(item.getSkuId()).getData();
            if (skuInfoEntity != null) {
                //计算价格
                return skuInfoEntity.getPrice().multiply(new BigDecimal(item.getCount()));
            }
            return new BigDecimal(0);
        }).reduce((t1, t2) -> t1.add(t2)).get();
        //计算页面总价和实时总价是否一致(此方法是为了防止价格的更新)
        BigDecimal totalPrice = orderSubmitVo.getTotalPrice();
        if(totalPrice.compareTo(currentTotalPrice)!=0){
            throw new OrderException("页面已经过期,请刷新后再试");
        }
        //校验库存
        /**
         * 检验库存:
         * 验库存:会让当前的库存数-锁定的库存>=所需的库存数
         * 锁库存:
         *  更新当前的库存数,加上当前的库存数
         */
        List<SkuLockVo> lockVos = ordersItem.stream().map(item -> {
            SkuLockVo skuLockVo = new SkuLockVo();
            skuLockVo.setSkuId(item.getSkuId());
            skuLockVo.setCount(item.getCount());
            skuLockVo.setOrderToken(orderSubmitVo.getOrderToken());
            return skuLockVo;
        }).collect(Collectors.toList());
        List<SkuLockVo> skuLockVoList = this.gmallWmsClient.checkAndCheck(lockVos).getData();
        if(!CollectionUtils.isEmpty(skuLockVoList)){
            throw new OrderException("手慢了，商品库存不足：" + JSON.toJSONString(skuLockVoList));
        }


        //下单
        UserInfo userInfo=LoginInterceptor.getUserInfo();
        Long userId = userInfo.getId();
        orderSubmitVo.setUserId(userId);
        OrderEntity orderEntity=null;
        try{
             orderEntity = this.gmallOmsClient.saveOrder(orderSubmitVo).getData();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("没有出现异常走了");
            //如果订单生成失败，立马释放库存
            this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","wms.unlock",orderToken);
            //如果订单生成成功,响应失败，标记该订单为失效订单

            this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","oms.close",orderToken);

        }
        //清除购物车
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        System.out.println("map:"+userId);
        List<Long> skuIds = ordersItem.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
        map.put("skuIds",JSON.toJSONString(skuIds));
        this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","cart.delete",map);
        System.out.println("order:"+orderEntity);
        return orderEntity;

    }
}
