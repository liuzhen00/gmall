package com.zl.gmall.search.feign;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.entity.*;
import com.zl.gmall.pms.feign.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-27 11:53
 */
@FeignClient("pms-service")
public interface GmallPmsFeign extends GmallPmsApi {

}
