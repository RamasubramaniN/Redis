package com.psg.rms.redis.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author rn5
 *
 */
public class JedisClient {
    
    private static JedisClient jedisClient;
    private JedisPool jedisPool;
    
    private static final String host = "127.0.0.1";
    private static final int port = 6379;
    private static final String password = "Admin@123";

    private JedisClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private static JedisPool getJedisConnectionPool() {
        synchronized (JedisClient.class) {
            if (jedisClient == null) {
                synchronized (JedisClient.class) {
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxWaitMillis(10000);// ms
                    jedisPoolConfig.setMaxTotal(15);
                    JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, 10000, password);
                    jedisClient = new JedisClient(jedisPool);
                }
            }
        }
        return jedisClient.jedisPool;
    }
    
    public static Jedis getJedis() {
        return getJedisConnectionPool().getResource();
    }
  
}
