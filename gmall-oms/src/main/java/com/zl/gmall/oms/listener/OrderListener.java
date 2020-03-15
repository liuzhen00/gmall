package com.zl.gmall.oms.listener;

import com.zl.gmall.oms.dao.OrderDao;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

/**
 * @author shkstart
 * @create 2020-03-12 0:30
 */
@Component
public class OrderListener {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AmqpTemplate amqpTemplate;

   @RabbitListener(bindings = @QueueBinding(
           value=@Queue(value="ORDER-CLOSE-QUEUE",durable = "true"),
           exchange = @Exchange(value = "ORDER-EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
           key={"oms.close","order.dead"}
   ))
    public void closeOrder(String orderToken){
       //只有订单是未支付时，才需要关单
       this.orderDao.closeOrder(orderToken);
   }

   @RabbitListener(bindings = @QueueBinding(
           value=@Queue(value = "ORDER-PAY-QUEUE",durable = "true"),
           exchange = @Exchange(value="ORDER-EXCHANGE",ignoreDeclarationExceptions = "true",type=ExchangeTypes.TOPIC),
           key={"order.pay"}

   ))
    public void paySuccess(String orderToken){
       System.out.println("lins:"+orderToken);
       if(this.orderDao.payOrder(orderToken)==1){
           //减库存
           System.out.println("开始减库存");
           this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","stock.minus",orderToken);

           //发送消息给ums加积分,根据订单编号查询订单
       }
   }


}
