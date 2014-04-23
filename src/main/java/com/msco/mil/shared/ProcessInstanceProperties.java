package com.msco.mil.shared;

import java.util.Date;
import com.msco.mil.shared.ProcessInstance;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;


public interface ProcessInstanceProperties extends PropertyAccess<ProcessInstance> {
    @Path("key")
    ModelKeyProvider<ProcessInstance> key();
    
    ValueProvider<ProcessInstance, Long> id();
    
    ValueProvider<ProcessInstance, String> name();
    
    ValueProvider<ProcessInstance, String> initiator();
    
    ValueProvider<ProcessInstance, String> version();
    
    ValueProvider<ProcessInstance, String> state();
    
    ValueProvider<ProcessInstance, Long> startDate();
    
    ValueProvider<ProcessInstance, Date> date();
    
    ValueProvider<ProcessInstance, String> externalId();
}