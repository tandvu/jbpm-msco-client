package com.msco.mil.shared;

import java.util.Date;
import com.msco.mil.shared.ProcessDefinition;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ProcessDefinitionProperties extends PropertyAccess<ProcessDefinition> {
    @Path("key")
    ModelKeyProvider<ProcessDefinition> key();
    
    ValueProvider<ProcessDefinition, Long> id();
    
    ValueProvider<ProcessDefinition, String> name();
    
    ValueProvider<ProcessDefinition, String> initiator();
    
    ValueProvider<ProcessDefinition, String> version();
    
    ValueProvider<ProcessDefinition, String> state();
    
    ValueProvider<ProcessDefinition, Long> startDate();
    
    ValueProvider<ProcessDefinition, Date> date();
}