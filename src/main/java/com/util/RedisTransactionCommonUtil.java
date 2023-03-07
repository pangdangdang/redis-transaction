package com.util;

import com.enums.RedisClientTypeEnum;

/**
 * 共用类
 */
public class RedisTransactionCommonUtil {


    //使用的redis客户端工具
    private static volatile RedisClientTypeEnum REDIS_CLIENT = RedisClientTypeEnum.REDISSON;

    //是否查询前镜像
    private static volatile boolean QUERY_PREV = false;



    public static boolean getQueryPrev() {
        return QUERY_PREV;
    }

    public static RedisClientTypeEnum getRedisClient() {
        return REDIS_CLIENT;
    }

    public static synchronized void setRedisClient(RedisClientTypeEnum redisClient) {
        REDIS_CLIENT = redisClient;
    }

    public static synchronized void setQueryPrev(boolean queryPrev) {
        QUERY_PREV = queryPrev;
    }

}