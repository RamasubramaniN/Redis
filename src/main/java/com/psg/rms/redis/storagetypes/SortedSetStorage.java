package com.psg.rms.redis.storagetypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.psg.rms.redis.client.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * @author rn5
 *
 */
public class SortedSetStorage {

    private final String studentRankKey = "StudentRank";

    public static void main(String[] args) {
        SortedSetStorage sortedSetStorage = new SortedSetStorage();
        sortedSetStorage.sortedSetExample();
    }

    private void sortedSetExample() {

        // Sorted set requires scores. You can use it as a priority queues.
        // Same keys with different jobs. Asending/Descending by priority.

        Jedis jedis = JedisClient.getJedis();
        jedis.zadd(studentRankKey, 1137, "Shanmuga Priya");
        jedis.zadd(studentRankKey, 1118, "Gomathi Jaya");
        jedis.zadd(studentRankKey, 1134, "Ramasubramani N");

        // You can add items in one go using a map.
        Map<String, Double> scoresMap = new HashMap<>();
        scoresMap.put("Vivek", 1159d);
        scoresMap.put("Vinoth", 1157d);
        scoresMap.put("Mohammed Shafi", 1163d);

        jedis.zadd(studentRankKey, scoresMap);

        Double ramScore = jedis.zscore(studentRankKey, "Ramasubramani N");

        System.out.println("Ram score : " + ramScore);
        long count = jedis.zcard(studentRankKey);
        System.out.println("Number of elements : " + count);

        Set<String> ascendingSet = jedis.zrange(studentRankKey, 0, -1);
        System.out.println("Marks in ascending order. " + ascendingSet);

        Set<String> descendingSet = jedis.zrevrange(studentRankKey, 0, -1);
        System.out.println("Marks in decending order. " + descendingSet);

        System.out.println("********** Scores in ascending order **********");
        Set<Tuple> tupleSetAsc = jedis.zrangeWithScores(studentRankKey, 0, -1);
        tupleSetAsc.forEach(tuple -> {
            System.out.println(tuple.getElement() + " - " + tuple.getScore());
        });

        System.out.println("********** Scores in descending order **********");
        Set<Tuple> tupleSetDesc = jedis.zrevrangeWithScores(studentRankKey, 0, -1);
        tupleSetDesc.forEach(tuple -> {
            System.out.println(tuple.getElement() + " - " + tuple.getScore());
        });
        
        jedis.close();
    }
}
