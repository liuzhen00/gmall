package com.zl.gmall.oms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-03-12 11:52
 */
@Configuration
public class RabbitConfig {

    //延时队列
    @Bean
    public Queue ttlQueue(){
        Map<String, Object> argument = new HashMap<>();
        argument.put("x-message-ttl",12000);
        argument.put("x-dead-letter-exchange","ORDER-EXCHANGE");
        argument.put("x-dead-letter-routing-key","order.dead");
        return new Queue("ORDER-TTL-QUEUE", true, false, false, argument);

    }

    //将延时队列绑定但交换机
    @Bean
    public Binding ttlBinding(){
        return new Binding("ORDER-TTL-QUEUE",Binding.DestinationType.QUEUE,
                "ORDER-EXCHANGE","order.ttl",null);
    }

}
