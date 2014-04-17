package com.msco.mil.shared;

import java.io.Serializable;


public class Deployment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String groupId;
    private String artifactId;
    private String version;
    private String kbaseName;
    private String ksessionName;
    private String strategy;
    private String status;
    private String identifier;
    
    private static int COUNTER = 0;
    
    
    public Deployment() {
        this.id = Integer.valueOf(COUNTER++);
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getKbaseName() {
        return kbaseName;
    }
    
    public void setKbaseName(String kbaseName) {
        this.kbaseName = kbaseName;
    }
    
    public String getKsessionName() {
        return ksessionName;
    }
    
    public void setKsessionName(String ksessionName) {
        this.ksessionName = ksessionName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getStrategy() {
        return strategy;
    }
    
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public static int getCOUNTER() {
        return COUNTER;
    }

    public static void setCOUNTER(int cOUNTER) {
        COUNTER = cOUNTER;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

     
}
