package com.zl.gmall.order.controller;

import com.alipay.api.AlipayApiException;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.oms.entity.OrderEntity;
import com.zl.gmall.order.config.AlipayTemplate;
import com.zl.gmall.order.vo.OrderConfirmVo;
import com.zl.gmall.order.service.OrderService;
import com.zl.gmall.oms.vo.OrderSubmitVo;
import com.zl.gmall.order.vo.PayAsyncVo;
import com.zl.gmall.order.vo.PayVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shkstart
 * @create 2020-03-10 13:32
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayTemplate aplTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @ApiOperation("添加订单")
    @GetMapping("/comfirm")
    public Resp<OrderConfirmVo> confirm(){
        OrderConfirmVo confirmVo=this.orderService.confirm();
        return Resp.ok(confirmVo);

    }

    @ApiOperation("生成订单")
    @PostMapping("/submit")
    public Resp<Object> submitOrder(@RequestBody OrderSubmitVo orderSubmitVo) {
        OrderEntity orderEntity = this.orderService.submitOrder(orderSubmitVo);
        System.out.println(orderEntity);

        try {
            PayVo pageVo=new PayVo();

            pageVo.setOut_trade_no(orderEntity.getOrderSn());
            pageVo.setTotal_amount(orderEntity.getTotalAmount().toString());
            pageVo.setBody("电商支付平台");
            pageVo.setSubject("电商网站");


            String form = this.aplTemplate.pay(pageVo);
            System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Resp.ok("生成订单完成");
    }

    //支付
    @PostMapping("pay/success")
    public Resp<Object> paySuccess(PayAsyncVo payAsyncVo){

        //发送订单消息,修改订单状态(支付成功时)支付的订单号
        System.out.println("執行了:"+payAsyncVo.getOut_trade_no());
        this.amqpTemplate.convertAndSend("ORDER-EXCHANGE","order.pay",payAsyncVo.getOut_trade_no());
        return Resp.ok(null);
    }



}
