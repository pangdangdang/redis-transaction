package com.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: tingmailang
 */
@Slf4j
@Getter
public enum RedisLockSuffixKeyTypeEnum {
    NO_SUFFIX("no_suffix", "没有后缀"),
    THREAD_LOCAL("thread_local", "通过ThreadLocal获取后缀"),
    PARAM("param", "通过参数获取后缀"),
    ;


    private final String code;
    private final String desc;

    RedisLockSuffixKeyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static RedisLockSuffixKeyTypeEnum of(String key) {
        Optional<RedisLockSuffixKeyTypeEnum> assetStatusEnum = Arrays.stream(RedisLockSuffixKeyTypeEnum.values())
                .filter(c -> c.getCode().equals(key)).findFirst();
        return assetStatusEnum.orElse(null);
    }


}
