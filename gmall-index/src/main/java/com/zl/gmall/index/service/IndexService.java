package com.zl.gmall.index.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.index.feign.GmallPmsClient;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.zl.gmall.pms.vo.CategoryVo;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shkstart
 * @create 2020-03-01 12:03
 */
@Service
public class IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX="index:cates:";

    public List<CategoryEntity> queryCategoryByPid() {
        Resp<List<CategoryEntity>> categoryResp = this.gmallPmsClient.queryCategoryByLebelorPid(1, null);
        return categoryResp.getData();
    }


    public List<CategoryVo> queryCategoryLevelByPid(Long pid) {
        String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX + pid);
        System.out.println(json);
        if(StringUtils.isNotBlank(json)){
            return JSON.parseArray(json,CategoryVo.class);
        }
        Resp<List<CategoryVo>> listResp =this.gmallPmsClient.queryCategoryByLebe2ByPid(pid);
        List<CategoryVo> categoryVoList = listResp.getData();
        if(!CollectionUtils.isEmpty((categoryVoList))){
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryVoList), 10, TimeUnit.DAYS);
        }

        return categoryVoList;
    }
}
