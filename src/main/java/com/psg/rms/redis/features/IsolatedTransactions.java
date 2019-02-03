package com.psg.rms.redis.features;

import java.util.List;
import java.util.stream.IntStream;

import com.psg.rms.redis.client.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author rn5 All the commands in a transaction are serialized and executed
 *         sequentially. It can never happen that a request issued by another
 *         client is served in the middle of the execution of a Redis
 *         transaction. This guarantees that the commands are executed as a
 *         single isolated operation. If you have a relational databases
 *         background, the fact that Redis commands can fail during a
 *         transaction, but still Redis will execute the rest of the transaction
 *         instead of rolling back, may look odd to you.
 *
 */
public class IsolatedTransactions {

    private static final String LIST_KEY = "List";

    /**
     * @param args
     */
    public static void main(String[] args) {
        IsolatedTransactions isolatedTransactions = new IsolatedTransactions();
        isolatedTransactions.transactionExample();
    }

    private void transactionExample() {
        Jedis jedis = JedisClient.getJedis();
        jedis.del(LIST_KEY);
        
        Transaction transaction = jedis.multi();

        IntStream.range(0, 11).forEach(index -> {
            transaction.set(String.valueOf(index), "TransactionTask" + (index * 2));
        });

        transaction.lpush(LIST_KEY, "1");
        transaction.lpush(LIST_KEY, "2");
        transaction.lpush(LIST_KEY, "3");
        transaction.lpush(LIST_KEY, "3");
        
        transaction.exec();

        System.out.println("Key : 1, Value : " + jedis.get("1"));
        System.out.println("Key : 9, Value : " + jedis.get("9"));

        System.out.println("********** List Elements **********");
        
        List<String> list = jedis.lrange(LIST_KEY, 0, jedis.llen(LIST_KEY));
        list.forEach(index -> {
            System.out.print(index + "\t");
        });
        
        jedis.close();
    }

}
