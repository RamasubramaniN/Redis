package com.psg.rms.redis.model;

/**
 * @author rn5
 *
 */
public class Student {
    private long id;
    private String name;
    private int marks;

    public Student(long id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }
}
