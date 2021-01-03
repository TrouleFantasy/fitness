package com.seeker.fitness.all.util.redis;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Configuration
public class RedisFactory {
    private static JedisPool jedisPool;
    private static String ip;
    private static int port;
    private static int maxTotal;
    private static int maxIdle;

    @Value("${redis.ip}")
    public  void setIp(String ip) {
        RedisFactory.ip = ip;
    }
    @Value("${redis.port}")
    public  void setPort(int port) {
        RedisFactory.port = port;
    }
    @Value("${redis.maxTotal}")
    public  void setMaxTotal(int maxTotal) {
        RedisFactory.maxTotal = maxTotal;
    }
    @Value("${redis.maxIdle}")
    public  void setMaxIdle(int maxIdle) {
        RedisFactory.maxIdle = maxIdle;
    }
    @PostConstruct
    private void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(maxTotal);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(maxIdle);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//        config.setMaxWaitMillis(waitMill);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
//        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config,ip,port);
    }

    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

}
