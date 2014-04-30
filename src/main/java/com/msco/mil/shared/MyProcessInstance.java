package com.msco.mil.shared;

import java.io.Serializable;
import java.util.Date;


public class MyProcessInstance implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer key;
    private Long id;
    private String name;
    private String initiator;
    private String version;
    private String state;
    private Long startDate;
    private String externalId;
    private Date date = new Date();
    private static int COUNTER = 0;
    
    public MyProcessInstance() {
        this.key = Integer.valueOf(COUNTER++);
    }
    
    public Integer getKey() {
        return key;
    }
    
    public void setKey(Integer key) {
        this.key = key;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getInitiator() {
        return initiator;
    }
    
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Long getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
