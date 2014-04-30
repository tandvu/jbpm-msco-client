package com.msco.mil.shared;

import java.util.Date;
import com.msco.mil.shared.MyProcessInstance;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;


public interface MyProcessInstanceProperties extends PropertyAccess<MyProcessInstance> {
    @Path("key")
    ModelKeyProvider<MyProcessInstance> key();
    
    ValueProvider<MyProcessInstance, Long> id();
    
    ValueProvider<MyProcessInstance, String> name();
    
    ValueProvider<MyProcessInstance, String> initiator();
    
    ValueProvider<MyProcessInstance, String> version();
    
    ValueProvider<MyProcessInstance, String> state();
    
    ValueProvider<MyProcessInstance, Long> startDate();
    
    ValueProvider<MyProcessInstance, Date> date();
    
    ValueProvider<MyProcessInstance, String> externalId();
}