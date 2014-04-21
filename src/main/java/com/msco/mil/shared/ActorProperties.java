package com.msco.mil.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.msco.mil.shared.Actor;


public interface ActorProperties extends PropertyAccess<Actor> {
    @Path("key")
    ModelKeyProvider<Actor> key();
    
    ValueProvider<Actor, String> name();
    
    ValueProvider<Actor, String> color();
}