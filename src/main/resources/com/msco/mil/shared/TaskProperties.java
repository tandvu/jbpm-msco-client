package com.msco.mil.shared;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;


public interface TaskProperties extends PropertyAccess<Task> {
    @Path("key")
    ModelKeyProvider<Task> key();
    
    ValueProvider<Task, Long> id();
    
    ValueProvider<Task, String> name();
    
    ValueProvider<Task, Integer> priority();
    
    ValueProvider<Task, String> status();
    
    ValueProvider<Task, Date> createdOn();
    
    ValueProvider<Task, Date> expiration();
    
    ValueProvider<Task, String> owner();
    
    ValueProvider<Task, String> deployment();
    
    ValueProvider<Task, String> parentName();
    
    ValueProvider<Task, String> action1();
}