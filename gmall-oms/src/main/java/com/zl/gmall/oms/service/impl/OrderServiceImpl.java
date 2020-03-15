package com.zl.gmall.oms.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.oms.dao.OrderItemDao;
import com.zl.gmall.oms.entity.OrderItemEntity;
import com.zl.gmall.oms.feign.GmallPmsClient;
import com.zl.gmall.oms.feign.GmallUmsClient;
import com.zl.gmall.oms.vo.OrderItemVo;
import com.zl.gmall.oms.vo.OrderSubmitVo;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.ums.entity.MemberEntity;
import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.oms.dao.OrderDao;
import com.zl.gmall.oms.entity.OrderEntity;
import com.zl.gmall.oms.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageVo(page);
    }

    @Transactional
    @Override
    public OrderEntity saveOrder(OrderSubmitVo submitVo) {
        //保存订单
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setMemberId(submitVo.getUserId());
        System.out.println("saveOrder:"+submitVo.getOrderToken());
        orderEntity.setOrderSn(submitVo.getOrderToken());
        orderEntity.setCreateTime(new Date());
        MemberEntity memberEntity = this.gmallUmsClient.queryMemberById(submitVo.getUserId()).getData();
        orderEntity.setMemberUsername(memberEntity.getUsername());
        orderEntity.setTotalAmount(submitVo.getTotalPrice());
        orderEntity.setPayType(submitVo.getPayType());
        orderEntity.setSourceType(0);
        orderEntity.setStatus(0);//未付款
        orderEntity.setDeliveryCompany(submitVo.getDeliveryCompany());
        //成长积分
        MemberReceiveAddressEntity address = submitVo.getAddress();
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());
        orderEntity.setReceiverCity(address.getCity());
        orderEntity.setUseIntegration(submitVo.getBounds());
        orderEntity.setDeleteStatus(0);
        this.save(orderEntity);
        //保存订单详情
        List<OrderItemVo> ordersItem = submitVo.getOrdersItem();
        if(!CollectionUtils.isEmpty(ordersItem)){
            ordersItem.forEach(item->{
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setOrderId(orderEntity.getId());
                orderEntity.setOrderSn(submitVo.getOrderToken());
                //查询sku信息
                SkuInfoEntity skuInfoEntity = this.gmallPmsClient.querySkuById(item.getSkuId()).getData();
                orderItemEntity.setSkuId(skuInfoEntity.getSkuId());
                orderItemEntity.setSkuPrice(skuInfoEntity.getPrice());
                orderItemEntity.setSkuPic(skuInfoEntity.getSkuDefaultImg());
                orderItemEntity.setSkuQuantity(item.getCount());
                orderItemEntity.setSkuName(skuInfoEntity.getSkuName());

                //查询销售属性
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = this.gmallPmsClient.querySkuSaleById(item.getSkuId()).getData();
                orderItemEntity.setSkuAttrsVals(JSON.toJSONString(saleAttrValueEntities));

                //查询Spu
                SpuInfoEntity spuInfoEntity = this.gmallPmsClient.querySpuById(skuInfoEntity.getSpuId()).getData();
                orderItemEntity.setSpuId(spuInfoEntity.getId());
                orderItemEntity.setSkuName(spuInfoEntity.getSpuName());
                orderItemEntity.setCategoryId(spuInfoEntity.getCatalogId());

                //查询spu的描述信息
                SpuInfoDescEntity spuInfoDescEntity = this.gmallPmsClient.querySpuDescById(spuInfoEntity.getId()).getData();
                orderItemEntity.setSkuPic(spuInfoDescEntity.getDecript());

                //查询商品名称
                BrandEntity brandEntity = this.gmallPmsClient.queryByBrandId(skuInfoEntity.getBrandId()).getData();
                orderItemEntity.setSpuBrand(brandEntity.getName());
                this.orderItemDao.insert(orderItemEntity);

            });
        }
        // 订单创建成功之后立马定时关单
        this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","order.ttl",submitVo.getOrderToken());
        System.out.println("oms:"+orderEntity);
        return orderEntity;
    }

}