package com.msco.mil.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DeploymentProperties extends PropertyAccess<Deployment> {
    @Path("id")
    ModelKeyProvider<Deployment> key();
     
    //(interface) ValueProvider<T,V>:  T: target object type, V: property type
    ValueProvider<Deployment, String> groupId();
    ValueProvider<Deployment, String> artifactId();
    ValueProvider<Deployment, String> identifier();
    ValueProvider<Deployment, String> version();
    ValueProvider<Deployment, String> kbaseName();
    ValueProvider<Deployment, String> ksessionName();
    ValueProvider<Deployment, String> strategy();
    ValueProvider<Deployment, String> status();
    
  }