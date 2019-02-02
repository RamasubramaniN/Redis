package com.psg.rms.redis.storagetypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.psg.rms.redis.client.JedisClient;
import com.psg.rms.redis.model.Legend;

import redis.clients.jedis.Jedis;

/**
 * @author rn5
 *
 */
public class HashStorage {
    
    //Redis hashes are hashmaps. So, we can store object properties in map. 
    //In our previous examples set/list we serialized objects to json to store it as string.
    //Hashes will be the better way of doing the same.
    
    private final String legendPrefix = "BattingLegends:";
    private final String idProperty = "id";
    private final String nameProperty = "name";
    private final String teamProperty = "team";
    private final String rankProperty = "rank";
    
    public static void main(String[] args) {
        HashStorage hashStorage = new HashStorage();
        hashStorage.hashExample();
    }
    
    private void hashExample() {
        Legend legend1 = new Legend(10L, "Sachin Tendulkar", "India", 1);
        Legend legend2 = new Legend(77L, "Sir Viv Richards", "Westindies", 2);
        Legend legend3 = new Legend(100L, "Virat Kohli", "India", 3);
        Legend legend4 = new Legend(12L, "De Villiers", "Southafrica", 4);
        Legend legend5 = new Legend(27L, "Ricky Ponting", "Australia", 5);
        Legend legend6 = new Legend(9L, "Kumar Sangakkara", "Srilanka", 6);
        Legend legend7 = new Legend(45L, "Jaques Kallies", "Southafrica", 7);
        Legend legend8 = new Legend(63L, "Michael Clarke", "Australia", 8);
        Legend legend9 = new Legend(5L, "Mike Hussey", "Australia", 9);
        Legend legend10 = new Legend(7L, "Mahendra Singh Dhoni", "India", 10);
        Legend legend11 = new Legend(1L, "Rahul Dravid", "India", 11);
        Legend legend12 = new Legend(99L, "Sourav Ganguly", "India", 12);
        Legend legend13 = new Legend(121L, "Kevin Peterson", "England", 13);
        Legend legend14 = new Legend(172L, "Kane Williamson", "Newzealand", 14);
        Legend legend15 = new Legend(900L, "Virendar Sehwag", "India", 15);
        
        List<Legend> legendList = new ArrayList<Legend>();
        legendList.add(legend1);
        legendList.add(legend2);
        legendList.add(legend3);
        legendList.add(legend4);
        legendList.add(legend5);
        legendList.add(legend6);
        legendList.add(legend7);
        legendList.add(legend8);
        legendList.add(legend9);
        legendList.add(legend10);
        legendList.add(legend11);
        legendList.add(legend12);
        legendList.add(legend13);
        legendList.add(legend14);
        legendList.add(legend15);
        
        Jedis jedis = JedisClient.getJedis();
        
        for(Legend legend : legendList) {
            Map<String, String> legendProperty = new HashMap<String, String>();
            legendProperty.put(idProperty, String.valueOf(legend.getId()));
            legendProperty.put(rankProperty, String.valueOf(legend.getRank()));
            legendProperty.put(nameProperty, legend.getPlayerName());
            legendProperty.put(teamProperty, legend.getTeam());
            jedis.hmset(legendPrefix + legend.getId(), legendProperty);
        }
        
       List<Long> idList = legendList.stream().map(legend -> legend.getId()).collect(Collectors.toList());
       List<Legend> legendListCache = new ArrayList<>();
       idList.forEach(id -> {
           Map<String, String> legendMap = jedis.hgetAll(legendPrefix + id);
           Legend legend = new Legend(Long.valueOf(legendMap.get(idProperty)), legendMap.get(nameProperty), legendMap.get(teamProperty), Integer.valueOf(legendMap.get(rankProperty)));
           legendListCache.add(legend);
       });
       legendList.forEach(legend -> System.out.println(legend));
    }
}
