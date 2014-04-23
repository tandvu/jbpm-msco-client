package com.msco.mil.shared;

import java.io.Serializable;


public class Actor implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer key;
    private String name;
    private String color;
    private static int COUNTER = 0;
    
    public Actor() {
        this.key = Integer.valueOf(COUNTER++);
    }
    
    public Integer getKey() {
        return key;
    }
    
    public void setKey(Integer key) {
        this.key = key;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
