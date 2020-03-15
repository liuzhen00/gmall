package com.zl.gmall.wms.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.wms.entity.WareSkuEntity;
import com.zl.gmall.wms.vo.SkuLockVo;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-27 11:47
 */

public interface GmallWmsApi {
    //6.根据skuid查询库存
    @GetMapping("wms/waresku/{skuId}")
    public Resp<List<WareSkuEntity>> queryWareSku(@PathVariable("skuId")Long skuId);

    @PostMapping("wms/waresku/check/lock")
    public Resp<List<SkuLockVo>> checkAndCheck(@RequestBody List<SkuLockVo> skuLockVos);
}
