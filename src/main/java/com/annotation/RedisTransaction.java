package com.annotation;

import java.lang.annotation.*;

/**
 * redis事务注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisTransaction {
    //是否自动清除RedisTransactionUtil
    boolean atuoRemove() default false;
    //如果使用RedisTemplate或者StringRedisTemplate操作集合删除，需要设置
    long index() default 0l;
}