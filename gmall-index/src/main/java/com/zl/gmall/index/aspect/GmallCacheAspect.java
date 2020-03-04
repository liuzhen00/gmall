package com.zl.gmall.index.aspect;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author shkstart
 * @create 2020-03-02 23:43
 */
@Aspect
@Component
public class GmallCacheAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.zl.gmall.index.aspect.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //方法拦截前执行
        //获取连接点的签名
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        //获取连接点的注解信息
        GmallCache gmallCache = signature.getMethod().getAnnotation(GmallCache.class);
        //获取注解中连接信息
        //获取前缀
        String prefix = gmallCache.prefix();
        //把前缀和方法签名拼接组成参数
        String param=Arrays.asList(joinPoint.getArgs()).toString();
        String key=prefix+ param;
        Class returnType = signature.getReturnType(); //获取方法的返回值类型
        //判断缓存中是否存在，如果不存在我们就枷锁
        String value = this.redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(value)){//如果不为空，则直接返回

            return JSON.parseObject(value,returnType);
        }
        //枷锁
        String lock = gmallCache.lock();
        RLock rlock = this.redissonClient.getLock(lock+param);
        //防止穿透枷锁
        rlock.lock();

        //再次检查内存中时候存在，因为高并发的情况下，可能在枷锁的这段时间内，已有其他线程放入内存中
        String value2 = this.redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(value2)){//如果不为空，则直接返回
            //释放锁
            rlock.unlock();

            return JSON.parseObject(value2,returnType);
        }

        //执行目标方法,并将查询的业务存入缓存中
        Object result = joinPoint.proceed(joinPoint.getArgs());
        int timeout = gmallCache.timeout();
        int random = gmallCache.random();
        this.redisTemplate.opsForValue().set(prefix + param, JSON.toJSONString(result), timeout + new Random().nextInt(random), TimeUnit.MINUTES);
        rlock.unlock();

        //方法拦截后通知

        return result;

    }

}
