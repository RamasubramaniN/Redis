package com.psg.rms.redis.storagetypes;

import java.util.Set;

import com.psg.rms.redis.client.JedisClient;

import redis.clients.jedis.Jedis;

/**
 * @author rn5
 *
 */
public class SetStorage {

    private static final String SET_KEY = "Set";

    /**
     * @param args
     */
    public static void main(String[] args) {
        SetStorage setStorage = new SetStorage();
        setStorage.performOperation();
    }

    private void performOperation() {
        Jedis jedis = JedisClient.getJedis();
        jedis.del(SET_KEY);
        
        jedis.sadd(SET_KEY, "1");
        jedis.sadd(SET_KEY, "2");
        jedis.sadd(SET_KEY, "3");
        jedis.sadd(SET_KEY, "3");//Duplicate value will not be accepted.
        
        System.out.print("Elements:\t");
        Set<String> elementSet = jedis.smembers(SET_KEY);//Get set value by key

        for(String element : jedis.smembers(SET_KEY)) {
            System.out.print(element + "\t");
        }
    }
    
}

