package com.enums;

import lombok.Getter;

/**
 * @description: redis操作类型
 * @author: tingmailang
 */
@Getter
public enum RedisClientTypeEnum {

    REDISSON("redisson"),
    REDIS_TEMPLATE("redisTemplate"),
    STRING_REDIS_TEMPLATE("stringRedisTemplate"),
    ;

    private String redisClientType;

    RedisClientTypeEnum(String redisClientType) {
        this.redisClientType = redisClientType;
    }
}
