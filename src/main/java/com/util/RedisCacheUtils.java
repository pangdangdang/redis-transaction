package com.util;

import com.enums.RedisOperateTypeEnum;
import com.exception.RedisTrancactionException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存工具类
 *
 * @author tingmailang
 */
@Slf4j
@Component
public class RedisCacheUtils<V> {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private void setRedisTransaction(String k, V v, V prev, RedisOperateTypeEnum redisOperateTypeEnum) {
        RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
        redisOperateUtil.setOperateTypeEnum(redisOperateTypeEnum);
        redisOperateUtil.setKey(k);
        redisOperateUtil.setPrevValue(prev);
        redisOperateUtil.setValue(v);
        RedisTransactionUtil.add(redisOperateUtil);
    }

    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean listAddTransaction(String k, V v) {
        try {
            if (v != null) {
                switch (RedisTransactionCommonUtil.getRedisClient()) {
                    case REDISSON:
                        RList<V> list = redissonClient.getList(k);
                        if (!list.contains(v)) {
                            list.add(v);
                            this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_ADD);
                        }
                        break;
                    case REDIS_TEMPLATE:
                        redisTemplate.opsForList().rightPush(k, v);
                        this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_ADD);
                        break;
                    case STRING_REDIS_TEMPLATE:
                        stringRedisTemplate.opsForList().rightPush(k, (String) v);
                        this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_ADD);
                        break;
                    default:
                        throw new RedisTrancactionException("unknown redis client");
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("redis setList fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean listAdd(String k, V v) {
        try {
            if (v != null) {
                switch (RedisTransactionCommonUtil.getRedisClient()) {
                    case REDISSON:
                        RList<V> list = redissonClient.getList(k);
                        if (!list.contains(v)) {
                            list.add(v);
                        }
                        break;
                    case REDIS_TEMPLATE:
                        redisTemplate.opsForList().rightPush(k, v);
                        break;
                    case STRING_REDIS_TEMPLATE:
                        stringRedisTemplate.opsForList().rightPush(k, (String) v);
                        break;
                    default:
                        throw new RedisTrancactionException("unknown redis client");
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("redis setList fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }


    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean listRemoveTransaction(String k, V v, Long index) {
        try {
            if (v != null) {
                switch (RedisTransactionCommonUtil.getRedisClient()) {
                    case REDISSON:
                        RList<V> list = redissonClient.getList(k);
                        if (!list.contains(v)) {
                            list.remove(v);
                            this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_REMOVE);
                        }
                        break;
                    case REDIS_TEMPLATE:
                        redisTemplate.opsForList().remove(k, index, v);
                        this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_REMOVE);
                        break;
                    case STRING_REDIS_TEMPLATE:
                        stringRedisTemplate.opsForList().rightPush(k, (String) v);
                        this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.LIST_REMOVE);
                        break;
                    default:
                        throw new RedisTrancactionException("unknown redis client");
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("redis deleteFromList fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean listRemove(String k, V v, Long index) {
        try {
            if (v != null) {
                switch (RedisTransactionCommonUtil.getRedisClient()) {
                    case REDISSON:
                        RList<V> list = redissonClient.getList(k);
                        if (!list.contains(v)) {
                            list.remove(v);
                        }
                        break;
                    case REDIS_TEMPLATE:
                        redisTemplate.opsForList().remove(k, index, v);
                        break;
                    case STRING_REDIS_TEMPLATE:
                        stringRedisTemplate.opsForList().rightPush(k, (String) v);
                        break;
                    default:
                        throw new RedisTrancactionException("unknown redis client");
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("redis deleteFromList fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean stringAddExpireTransaction(String k, V v, Long timeOut, TimeUnit timeUnit) {
        try {
            if (v == null) {
                return false;
            }
            V prev = null;
            switch (RedisTransactionCommonUtil.getRedisClient()) {
                case REDISSON:
                    RBucket<V> bucket = redissonClient.getBucket(k);
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = bucket.get();
                    }
                    bucket.set(v);
                    bucket.expire(timeOut, timeUnit);
                    break;
                case REDIS_TEMPLATE:
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = (V) redisTemplate.opsForValue().get(k);
                    }
                    redisTemplate.opsForValue().set(k, v);
                    break;
                case STRING_REDIS_TEMPLATE:
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = (V) stringRedisTemplate.opsForValue().get(k);
                    }
                    stringRedisTemplate.opsForValue().set(k, (String) v);
                    break;
                default:
                    throw new RedisTrancactionException("unknown redis client");
            }
            this.setRedisTransaction(k, v, prev, RedisOperateTypeEnum.STRING_SET);
            return true;
        } catch (Exception e) {
            log.warn("redis setListExpire fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * v
     *
     * @return true 成功 false 失败
     */
    public boolean stringAddTransaction(String k, V v) {
        try {
            if (v == null) {
                return false;
            }
            V prev = null;
            switch (RedisTransactionCommonUtil.getRedisClient()) {
                case REDISSON:
                    RBucket<V> bucket = redissonClient.getBucket(k);
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = bucket.get();
                    }
                    bucket.set(v);
                    break;
                case REDIS_TEMPLATE:
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = (V) redisTemplate.opsForValue().get(k);
                    }
                    redisTemplate.opsForValue().set(k, v);
                    break;
                case STRING_REDIS_TEMPLATE:
                    if (RedisTransactionCommonUtil.getQueryPrev()) {
                        prev = (V) stringRedisTemplate.opsForValue().get(k);
                    }
                    stringRedisTemplate.opsForValue().set(k, (String) v);
                    break;
                default:
                    throw new RedisTrancactionException("unknown redis client");
            }
            this.setRedisTransaction(k, v, prev, RedisOperateTypeEnum.STRING_SET);
            return true;
        } catch (Exception e) {
            log.warn("redis setValue fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * v
     *
     * @return true 成功 false 失败
     */
    public boolean stringAdd(String k, V v) {
        try {
            if (v == null) {
                return false;
            }
            switch (RedisTransactionCommonUtil.getRedisClient()) {
                case REDISSON:
                    RBucket<V> bucket = redissonClient.getBucket(k);
                    bucket.set(v);
                    break;
                case REDIS_TEMPLATE:
                    redisTemplate.opsForValue().set(k, v);
                    break;
                case STRING_REDIS_TEMPLATE:
                    stringRedisTemplate.opsForValue().set(k, (String) v);
                    break;
                default:
                    throw new RedisTrancactionException("unknown redis client");
            }
            return true;
        } catch (Exception e) {
            log.warn("redis setValue fail, key = {}, value = {}, e = {}", k, v, e);
            return false;
        }
    }

    /**
     * v
     *
     * @return true 成功 false 失败
     */
    public boolean stringDeleteTransaction(String k, V v) {
        try {
            switch (RedisTransactionCommonUtil.getRedisClient()) {
                case REDISSON:
                    RBucket<V> bucket = redissonClient.getBucket(k);
                    if (bucket.isExists()) {
                        bucket.delete();
                    }
                    break;
                case REDIS_TEMPLATE:
                    redisTemplate.opsForValue().getAndDelete(k);
                    break;
                case STRING_REDIS_TEMPLATE:
                    stringRedisTemplate.opsForValue().getAndDelete(k);
                    break;
                default:
                    throw new RedisTrancactionException("unknown redis client");
            }
            this.setRedisTransaction(k, v, null, RedisOperateTypeEnum.STRING_DELETE);
            return true;
        } catch (Exception e) {
            log.warn("redis deleteValue fail, key = {}, value = {}, e = {}", k, e);
            return false;
        }
    }

    /**
     * v
     *
     * @return true 成功 false 失败
     */
    public boolean stringDelete(String k) {
        try {
            switch (RedisTransactionCommonUtil.getRedisClient()) {
                case REDISSON:
                    RBucket<V> bucket = redissonClient.getBucket(k);
                    if (bucket.isExists()) {
                        bucket.delete();
                    }
                    break;
                case REDIS_TEMPLATE:
                    redisTemplate.opsForValue().getAndDelete(k);
                    break;
                case STRING_REDIS_TEMPLATE:
                    stringRedisTemplate.opsForValue().getAndDelete(k);
                    break;
                default:
                    throw new RedisTrancactionException("unknown redis client");
            }
            return true;
        } catch (Exception e) {
            log.warn("redis deleteValue fail, key = {}, value = {}, e = {}", k, e);
            return false;
        }
    }

}
