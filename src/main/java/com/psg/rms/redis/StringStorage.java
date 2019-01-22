package com.psg.rms.redis;

import com.psg.rms.redis.client.JedisClient;

import redis.clients.jedis.Jedis;

/**
 * @author rn5
 *
 */
public class StringStorage {
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        StringStorage stringStorage = new StringStorage();
        stringStorage.performStringOperation1();
        stringStorage.performStringOperation2();
    }

    public void performStringOperation1() throws Exception {
        Jedis jedis = JedisClient.getJedis();
        jedis.set("foo", "bar");
        System.out.println(jedis.get("foo"));//bar
        jedis.expire("foo", 1);//expire foo key in 1 second.
        Thread.sleep(1000);//Sleep for 1 second.
        System.out.println(jedis.get("foo"));//Key expired. So, 'null' will be printed.
    }

    private void performStringOperation2() {
        Jedis jedis = JedisClient.getJedis();
        jedis.set("count", "97");
        jedis.incr("count");
        jedis.incr("count");
        System.out.println(jedis.get("count"));//99
        jedis.decr("count");
        jedis.decr("count");
        System.out.println(jedis.get("count"));//97
        
        jedis.set("foo", "bar");
        
        System.out.println("Total keys = " + jedis.dbSize());//Total keys = 2
        jedis.flushAll(); //Clear all keys
        System.out.println("Total keys = " + jedis.dbSize());
    }
}
