package com.example.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author jeymingwu
 * @date 2021/4/3 0:05
 */
public class SimpleRateLimiter {

    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 简单限流策略
     * @param usedId 指定用户ID
     * @param actionKey 某个行为
     * @param period 特定的时间内
     * @param maxCount 允许发生最多次数
     * @return true or false
     */
    public boolean isActionAllowed(String usedId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", usedId, actionKey);
        long nowTs = System.currentTimeMillis();
        Pipeline pipe = this.jedis.pipelined();
        pipe.multi();
        pipe.zadd(key, nowTs, "" + nowTs);
        pipe.zremrangeByScore(key, 0, nowTs - period * 1000);
        Response<Long> count = pipe.zcard(key);
        pipe.expire(key, period + 1);
        pipe.exec();
        pipe.close();
        return count.get() <= maxCount;
    }
}
