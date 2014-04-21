package com.msco.mil.client.sencha.gxt.examples.resources.client.model;

import java.io.Serializable;

public class Friend implements Serializable {
	private String name;
	private int age;
	private boolean isMale;
	private Integer id;
	
	private static int COUNTER = 0;

	public Friend() {
	    this.id = Integer.valueOf(COUNTER++);
	  }
	
	public Friend (String name, int age, boolean isMale)
	{
		this();
		this.name = name;
		this.age = age;
		this.isMale = isMale;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

}
