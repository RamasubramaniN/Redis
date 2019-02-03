/**
 * 
 */
package com.psg.rms.redis.storagetypes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.psg.rms.redis.client.JedisClient;
import com.psg.rms.redis.model.Employee;

import redis.clients.jedis.Jedis;

/**
 * @author rn5
 *
 */
public class ListStorage {

    private static final String NUMBERS_KEY = "Numbers";
    private static final String EMPLOYEE_KEY = "Employees";

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListStorage listStorage = new ListStorage();
        listStorage.stringListExample();
        listStorage.employeeListExample();
    }

    private void employeeListExample() {
        
        System.out.println("********* Employee Operation *********");
        
        Employee employee1 = new Employee(1L, "Ramasubramani", 29);
        Employee employee2 = new Employee(1L, "Vivek", 29);
        Employee employee3 = new Employee(1L, "Vinoth", 30);
        Employee employee4 = new Employee(1L, "Shafi", 30);
        
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);
        employeeList.add(employee4);
        
        Jedis jedis = JedisClient.getJedis();
        jedis.del(EMPLOYEE_KEY);
        boolean keyExists = jedis.exists(EMPLOYEE_KEY);
        System.out.println("Employee Exists in the cache : " + keyExists);
        
        System.out.println("Adding employees to cache");
        Gson gson = new Gson();
        for(Employee employee : employeeList) {
            jedis.lpush(EMPLOYEE_KEY, gson.toJson(employee));
        }
        
        long numberOfEmployees = jedis.llen(EMPLOYEE_KEY);
        System.out.println("Number of Employees : " + numberOfEmployees);
        
        System.out.println("Getting Employees from cache");
        List<String> employeeJsonList = jedis.lrange(EMPLOYEE_KEY, 0, numberOfEmployees);
        for(String employeeJson : employeeJsonList) {
            Employee employee = gson.fromJson(employeeJson, Employee.class);
            System.out.println(employee);
        }
    }

    private void stringListExample() {
        System.out.println("********* Number Operation *********");
        Jedis jedis = JedisClient.getJedis();
        jedis.del(NUMBERS_KEY);
        jedis.lpush(NUMBERS_KEY, "1");
        jedis.lpush(NUMBERS_KEY, "2");
        jedis.lpush(NUMBERS_KEY, "3");
        jedis.lpush(NUMBERS_KEY, "3");//Duplicates allowed.

        System.out.println("Length of the list : " + jedis.llen(NUMBERS_KEY));
        List<String> list = jedis.lrange(NUMBERS_KEY, 0, jedis.llen(NUMBERS_KEY));
        System.out.print("All elements by range :\t");
        for (String element : list) {
            System.out.print(element + "\t");//3    3   2   1
        }
        
        System.out.print("\nNone of the elements are poped out now. So list size : " + jedis.llen(NUMBERS_KEY));
       
        String element = jedis.lindex(NUMBERS_KEY, 1);
        System.out.print("\nElement at index 1 : " + element);
        
        System.out.print("\nAll elements by pop :\t");
        while((element = jedis.lpop(NUMBERS_KEY)) != null) {
            System.out.print(element + "\t");//3    3   2   1
        }
        
        System.out.println("\nAll elements are poped out now. So list size : " + jedis.llen(NUMBERS_KEY));
        
        jedis.close();
    }

}