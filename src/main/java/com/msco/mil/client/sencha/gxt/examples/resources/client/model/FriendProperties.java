package com.msco.mil.client.sencha.gxt.examples.resources.client.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface FriendProperties extends PropertyAccess<Stock> {
	  
	//ModelkeyProviders are reponsible for returning a unique key for a given model
	  @Path("id")
	  ModelKeyProvider<Friend> key();
	  
	  @Path("name")
	  LabelProvider<Friend> nameLabel();
	  
	  ValueProvider<Friend, String> name();
	  ValueProvider<Friend, Integer> age();
	  ValueProvider<Friend, Boolean> isMale();
	  
	  
}
