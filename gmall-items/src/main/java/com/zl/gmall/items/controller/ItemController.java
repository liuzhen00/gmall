package com.zl.gmall.items.controller;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.items.service.ItemService;
import com.zl.gmall.items.vo.ItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shkstart
 * @create 2020-03-03 22:41
 */
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("{skuId}")
    public Resp<ItemVo> queryItemBySkuId(@PathVariable("skuId")Long skuId){
        ItemVo itemVo= itemService.queryItemBySkuId(skuId);
        return Resp.ok(itemVo);
    }
}
