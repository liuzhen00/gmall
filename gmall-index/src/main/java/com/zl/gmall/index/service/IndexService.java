package com.zl.gmall.index.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.index.aspect.GmallCache;
import com.zl.gmall.index.feign.GmallPmsClient;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.zl.gmall.pms.vo.CategoryVo;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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

    @Autowired
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX="index:cates:";

    public List<CategoryEntity> queryCategoryByPid() {
        Resp<List<CategoryEntity>> categoryResp = this.gmallPmsClient.queryCategoryByLebelorPid(1, null);
        return categoryResp.getData();
    }

   @GmallCache(prefix = "index:cates:", timeout = 14400, random = 3600, lock = "lock")
    public List<CategoryVo> queryCategoryLevelByPid(Long pid) {
       /* String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX + pid);
        System.out.println(json);
        if(StringUtils.isNotBlank(json)){
            return JSON.parseArray(json,CategoryVo.class);
        }*/
        Resp<List<CategoryVo>> listResp =this.gmallPmsClient.queryCategoryByLebe2ByPid(pid);
        List<CategoryVo> categoryVoList = listResp.getData();
        /*if(!CollectionUtils.isEmpty((categoryVoList))){
            //为了防止缓存雪崩，给过期时间加上一个随机值
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryVoList), 10+(int)(Math.random()*10), TimeUnit.DAYS);
        }else{
            //为了防止缓存击穿，将将为空的数据也存入缓存中，并且设置过期时间
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryVoList), 5+(int)(Math.random()*10), TimeUnit.DAYS);
        }*/

        return categoryVoList;
    }

    //分布式锁测试
    public  void testLock()  {
        // Redis的分布式测试的解决方案
        //如果不存在就返回1就表示true  如果存在是0表示false 可以用setnx表示
        //该方法解决会带来：如果在刚好获取到锁，业务逻辑出现了问题，导致了锁无法释放
        //解决方案：设置锁自动过期
        //但是此种方案：会导致释放其他人的锁，添加UUID，在释放锁的时候检测该锁是否是自己的锁，如果一致则进行删除
//        String uuid= UUID.randomUUID().toString();
//        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,
//               30,TimeUnit.SECONDS);
//        // 查询redis中的num值
//        if(flag) {
        RLock lock = redissonClient.getLock("lock");  //枷锁和释放锁只要是同一一个名词就是了
        lock.lock(); //枷锁
        String value = this.stringRedisTemplate.opsForValue().get("num");
            // 没有该值return
            if (StringUtils.isBlank(value)) {
                return;
            }
            // 有值就转成成int
            int num = Integer.parseInt(value);
            // 把redis中的num值+1
            this.stringRedisTemplate.opsForValue().set("num", String.valueOf(++num));

            //释放锁
        lock.unlock();  //释放锁

            //释放锁,这种做法缺乏原子性，所以使用lua脚本解决
//            if(StringUtils.equals( stringRedisTemplate.opsForValue().get("lock"),uuid)){
//                stringRedisTemplate.delete("lock");
//            }
            //释放锁
            // 2. 释放锁 del
//            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//            this.stringRedisTemplate.execute(new DefaultRedisScript<>(script), Arrays.asList("lock"), Arrays.asList(uuid));

        /*}else{
            //表示存在
            try {
                Thread.sleep(3000);
                testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
