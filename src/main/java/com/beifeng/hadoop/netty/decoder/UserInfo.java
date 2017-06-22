package com.beifeng.hadoop.netty.decoder;

import java.io.Serializable;

import org.msgpack.annotation.Message;

@Message
public class UserInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private int id;
    
    private String name;
    
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    
    public UserInfo() {
        super();
    }

    public UserInfo(int id, String name, int age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}
