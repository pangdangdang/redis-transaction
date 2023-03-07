package com.core;

import com.annotation.RedisTransaction;
import com.util.RedisCacheUtils;
import com.util.RedisOperateUtil;
import com.util.RedisTransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Stack;

/**
 * redis事务，切面处理类
 */
@Slf4j
@Aspect
@Component
public class RedisTransactionAspect {

    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Pointcut("@annotation(com.annotation.RedisTransaction)")
    public void transactionPointCut() {
    }

    @AfterReturning("transactionPointCut() && @annotation(redisTransaction)")
    public void afterReturning(RedisTransaction redisTransaction) {
        //如果方法注解中开启自动清除，就去除
        if (redisTransaction.atuoRemove()) {
            RedisTransactionUtil.remove();
            log.info("自动清除RedisTransactionUtil:{}", Thread.currentThread().getName());
        }
    }

    @AfterThrowing("transactionPointCut() && @annotation(redisTransaction)")
    public void afterThrowing(RedisTransaction redisTransaction) {
        try {
            Stack<RedisOperateUtil> stack = RedisTransactionUtil.get();
            if (Objects.isNull(stack)) {
                return;
            }
            log.info("redis回滚:{}", stack);
            RedisOperateUtil redisOperateUtil;
            while (!stack.isEmpty()) {
                redisOperateUtil = stack.pop();
                switch (redisOperateUtil.getOperateTypeEnum()) {
                    case STRING_ADD:
                        redisCacheUtils.stringDelete(redisOperateUtil.getKey());
                        break;
                    case STRING_DELETE:
                        redisCacheUtils.stringAdd(redisOperateUtil.getKey(), redisOperateUtil.getValue());
                        break;
                    case LIST_ADD:
                        redisCacheUtils.listRemove(redisOperateUtil.getKey(), redisOperateUtil.getValue());
                        break;
                    case LIST_REMOVE:
                        redisCacheUtils.listAdd(redisOperateUtil.getKey(), redisOperateUtil.getValue());
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            log.error("reidis回滚失败:{}", e);
        } finally {
            //如果方法注解中开启自动清除，就去除
            if (redisTransaction.atuoRemove()) {
                RedisTransactionUtil.remove();
                log.info("自动清除RedisTransactionUtil:{}", Thread.currentThread().getName());
            }
        }
    }

}