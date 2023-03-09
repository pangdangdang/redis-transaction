# redis-transaction

#### 介绍
基于SpringBoot的轻量级redis事务回滚机制，使用栈和ThreadLocal记录业务链的redis操作，发生异常进行回滚

#### 软件架构
SpringBoot
![image](https://user-images.githubusercontent.com/90837835/224013720-ae29dbcd-69eb-4a47-ac0b-ab06ce4fcac7.png)



#### 使用说明

    1、目前支持链接redis的工具主要是Redisson、RedisTemplate、StringRedisTemplate，默认Redisson
    
    2、进行回滚时需要考虑是否要进行查询前镜像，可以通过设置RedisTransactionCommonUtil的QUERY_PREV属性
    
    3、需要使用RedisTransactionCacheUtils操作需要回滚的redis数据，此时加入threadlocal
    
    
    使用方式：
    
    @Resource
    private RedisTransactionCacheUtils redisTransactionCacheUtils;

    @RedisTransaction(atuoRemove = true)
    public void shopAdd(ShopOnlineDTO request) {
        //业务处理
        redisTransactionCacheUtils.stringSetTransaction(request.getShopId(),request.getPrice());
        //业务处理
        redisTransactionCacheUtils.stringSetTransaction(request.getShopId(),request.getLocation());
        //业务处理
    }
    
    设置redis客户端为RedisTemplate
    RedisTransactionCommonUtil.setRedisClient(RedisClientTypeEnum.REDIS_TEMPLATE);
    
    设置查询前镜像
    RedisTransactionCommonUtil.setQueryPrev(true);
    
    
#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

