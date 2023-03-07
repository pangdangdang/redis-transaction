package com.util;


import java.util.Objects;
import java.util.Stack;

/**
 * redis事务工具类
 *
 * @author tingmailang
 */
public class RedisTransactionUtil {

    static ThreadLocal<Stack> TRANSACTION_QUEUE = new ThreadLocal<Stack>();

    public static void add(RedisOperateUtil redisOperateUtil) {
        if (Objects.isNull(TRANSACTION_QUEUE.get())) {
            Stack stack = new Stack();
            TRANSACTION_QUEUE.set(stack);
        }
        TRANSACTION_QUEUE.get().add(redisOperateUtil);
    }
    public static Stack get() {
        return TRANSACTION_QUEUE.get();
    }
    public static void remove() {
        TRANSACTION_QUEUE.remove();
    }
}
