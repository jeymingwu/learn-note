package com.example.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Redis事务Demo
 * @author jeymingwu
 * @date 2021/4/9 11:19
 */
public class RedisTransactionDemo {

    public static String keyFor(String userId) {
        return String.format("account_%s", userId);
    }

    public static int doubleAccount(Jedis jedis, String userId) {
        String key = keyFor(userId);
        while (true) {
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *= 2; // 加倍
            Transaction tx = jedis.multi(); //开启事务
            tx.set(key, String.valueOf(value));
            List<Object> res = tx.exec();
            if (res != null) { // 事务执行成功
                break;
            }
        }
        return Integer.parseInt(jedis.get(key));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        String userId = "abc";
        String key = keyFor(userId);
        jedis.set(key, String.valueOf(10));

        System.out.println(doubleAccount(jedis, userId));
        jedis.close();
    }
}
