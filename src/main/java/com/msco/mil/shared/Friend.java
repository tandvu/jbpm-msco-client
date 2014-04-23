package com.msco.mil.shared;

import java.io.Serializable;


public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;

	private int age;
    private String name;
    private boolean isMale;
    private Integer key;
    private static int COUNTER = 0;
    
    public Friend(String name, int age, boolean isMale)
    {
    	this();
    	this.name = name;
    	this.age = age;
    	this.isMale = isMale;
    }
    
    public Friend() {
        this.key = Integer.valueOf(COUNTER++);
    }
    
    public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

}
