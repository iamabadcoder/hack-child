package com.hackx.spark;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = -8903040781999605934L;

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
