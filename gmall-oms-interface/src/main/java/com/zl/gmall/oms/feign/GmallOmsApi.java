package com.zl.gmall.oms.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.oms.entity.OrderEntity;
import com.zl.gmall.oms.vo.OrderSubmitVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shkstart
 * @create 2020-03-11 22:29
 */
public interface GmallOmsApi {

    @PostMapping("oms/order/saveOrder")
    public Resp<OrderEntity> saveOrder(@RequestBody OrderSubmitVo orderSubmitVo);
}
