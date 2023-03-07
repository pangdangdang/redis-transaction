package com.util;

import com.enums.RedisOperateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
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

    /**
     * list
     *
     * @return true 成功 false 失败
     */
    public boolean listAddTransaction(String k, V v) {
        try {
            if (v != null) {
                RList<V> list = redissonClient.getList(k);
                if (!list.contains(v)) {
                    list.add(v);
                    RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
                    redisOperateUtil.setOperateTypeEnum(RedisOperateTypeEnum.LIST_ADD);
                    redisOperateUtil.setKey(k);
                    redisOperateUtil.setValue(v);
                    RedisTransactionUtil.add(redisOperateUtil);
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
                RList<V> list = redissonClient.getList(k);
                if (!list.contains(v)) {
                    list.add(v);
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
    public boolean listRemoveTransaction(String k, V v) {
        try {
            if (v != null) {
                RList<V> list = redissonClient.getList(k);
                if (list.contains(v)) {
                    list.remove(v);
                    RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
                    redisOperateUtil.setOperateTypeEnum(RedisOperateTypeEnum.LIST_REMOVE);
                    redisOperateUtil.setKey(k);
                    redisOperateUtil.setValue(v);
                    RedisTransactionUtil.add(redisOperateUtil);
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
    public boolean listRemove(String k, V v) {
        try {
            if (v != null) {
                RList<V> list = redissonClient.getList(k);
                if (list.contains(v)) {
                    list.remove(v);
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
            RBucket<V> bucket = redissonClient.getBucket(k);
            bucket.set(v);
            bucket.expire(timeOut, timeUnit);
            RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
            redisOperateUtil.setOperateTypeEnum(RedisOperateTypeEnum.STRING_ADD);
            redisOperateUtil.setKey(k);
            redisOperateUtil.setValue(v);
            RedisTransactionUtil.add(redisOperateUtil);
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
            RBucket<V> bucket = redissonClient.getBucket(k);
            bucket.set(v);
            RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
            redisOperateUtil.setOperateTypeEnum(RedisOperateTypeEnum.STRING_ADD);
            redisOperateUtil.setKey(k);
            redisOperateUtil.setValue(v);
            RedisTransactionUtil.add(redisOperateUtil);
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
            RBucket<V> bucket = redissonClient.getBucket(k);
            bucket.set(v);
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
            RBucket<V> bucket = redissonClient.getBucket(k);
            if (bucket.isExists()) {
                bucket.delete();
                RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
                redisOperateUtil.setOperateTypeEnum(RedisOperateTypeEnum.STRING_DELETE);
                redisOperateUtil.setKey(k);
                redisOperateUtil.setValue(v);
                RedisTransactionUtil.add(redisOperateUtil);
            }
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
            RBucket<V> bucket = redissonClient.getBucket(k);
            if (bucket.isExists()) {
                bucket.delete();
            }
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
    public V getValue(String k) {
        try {
            RBucket<V> redisMacShopId = redissonClient.getBucket(k);
            if (redisMacShopId.isExists()) {
                return redisMacShopId.get();
            }
            return null;
        } catch (Exception e) {
            log.warn("redis getValue fail, key = {}, value = {}, e = {}", k, e);
            return null;
        }
    }

}
