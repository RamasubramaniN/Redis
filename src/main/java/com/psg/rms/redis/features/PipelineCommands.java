package com.psg.rms.redis.features;

import java.util.Set;
import java.util.stream.IntStream;

import com.psg.rms.redis.client.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author rn5
 * 
 *         Sometimes you need to send a bunch of different commands. A very cool
 *         way to do that, and have better performance than doing it the naive
 *         way, is to use pipelining. This way you send commands without waiting
 *         for response, and you actually read the responses at the end, which
 *         is faster. It is a network optimization if all operations are
 *         mutually independent.
 *
 */
public class PipelineCommands {

    private static final String setKey = "set";

    /**
     * @param args
     */
    public static void main(String[] args) {
        PipelineCommands pipelineCommands = new PipelineCommands();
        pipelineCommands.pipelineExample();
    }

    private void pipelineExample() {
        System.out.println("********* Pipeline Commands **********");
        Jedis jedis = JedisClient.getJedis();
        Pipeline pipeline = jedis.pipelined(); // Create a pipeline for sending bunch of commands.

        // All write commands. Instead of jedis, give the commands to pipeline.

        // Set values (1, Task1), (2, Task2) ... (10, Task10)
        IntStream.range(1, 10).forEach(index -> {
            pipeline.set(String.valueOf(index), "Task" + index);
        });

        // Cache a set
        pipeline.sadd(setKey, "1");
        pipeline.sadd(setKey, "2");
        pipeline.sadd(setKey, "3");
        pipeline.sadd(setKey, "3");

        // All Read commands.
        Response<Set<String>> setResponse = pipeline.smembers(setKey);
        Response<String> valueResponse1 = pipeline.get("1");
        Response<String> valueResponse9 = pipeline.get("9");

        pipeline.sync(); // Send all commands at once.

        System.out.println("Key : 1, Value : " + valueResponse1.get());
        System.out.println("Key : 9, Value : " + valueResponse9.get());

        System.out.println("********** Set Elements **********");
        Set<String> setValue = setResponse.get();
        setValue.forEach(element -> {
            System.out.print(element + "\t");
        });
        
        jedis.close();
    }

}
