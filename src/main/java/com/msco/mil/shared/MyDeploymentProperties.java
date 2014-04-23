package com.msco.mil.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.msco.mil.shared.MyDeployment;

public interface MyDeploymentProperties extends PropertyAccess<MyDeployment> {
    @Path("id")
    ModelKeyProvider<MyDeployment> key();
     
    //(interface) ValueProvider<T,V>:  T: target object type, V: property type
    ValueProvider<MyDeployment, String> groupId();
    ValueProvider<MyDeployment, String> artifactId();
    ValueProvider<MyDeployment, String> identifier();
    ValueProvider<MyDeployment, String> version();
    ValueProvider<MyDeployment, String> kbaseName();
    ValueProvider<MyDeployment, String> ksessionName();
    ValueProvider<MyDeployment, String> strategy();
    ValueProvider<MyDeployment, String> status();
    
  }