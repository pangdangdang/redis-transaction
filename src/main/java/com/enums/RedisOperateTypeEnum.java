package com.enums;

import lombok.Getter;

/**
 * @description: redis操作类型
 * @author: tingmailang
 */
@Getter
public enum RedisOperateTypeEnum {

    STRING_SET("string", "add"),
    STRING_DELETE("string", "delete"),
    LIST_ADD("list", "add"),
    LIST_REMOVE("list", "remove"),
    ;

    private String dataType;
    private String operateType;

    RedisOperateTypeEnum(String key, String value) {
        this.dataType = key;
        this.operateType = value;
    }
}
