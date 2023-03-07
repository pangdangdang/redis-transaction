package com.enums;

import lombok.Getter;

/**
 * @description: redis操作类型
 * @author: tingmailang
 */
@Getter
public enum RedisOperateTypeEnum {

    STRING_ADD("string", "add"),
    STRING_DELETE("string", "delete"),
    LIST_ADD("list", "add"),
    LIST_REMOVE("list", "remove"),
    HASH_ADD("hash", "add"),
    HASH_REMOVE("hash", "remove"),
    SET_ADD("set", "add"),
    SET_REMOVE("set", "remove"),
    ZSET_ADD("zset", "add"),
    ZSET_REMOVE("zset", "remove"),
    ;

    private String dataType;
    private String operateType;

    RedisOperateTypeEnum(String key, String value) {
        this.dataType = key;
        this.operateType = value;
    }
}
