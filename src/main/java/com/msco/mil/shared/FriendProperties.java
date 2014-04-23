package com.msco.mil.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.msco.mil.shared.Actor;


public interface FriendProperties extends PropertyAccess<Actor> {
    @Path("key")
    ModelKeyProvider<Friend> key();
    
    ValueProvider<Friend, String> name();
    ValueProvider<Friend, Integer> age();
    ValueProvider<Friend, Boolean> isMale();

}