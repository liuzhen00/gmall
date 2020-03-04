package com.zl.gmall.index.aspect;

import java.lang.annotation.*;

/**
 * @author shkstart
 * @create 2020-03-02 23:31
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    /**
     * 缓存前缀
     * @return
     */
    String prefix() default "";

    /**
     * 设置缓存的有效时间
     * @return
     */
    int timeout() default 5;

    /**
     * 这是缓存雪崩的随机范围值
     * @return
     */
    int random() default 5;

    /**
     * 防止击穿的 分布式锁Key
     * @return
     */
    String lock() default "lock";


}
