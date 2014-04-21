/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.msco.mil.client.sencha.gxt.examples.resources.client.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface BaseDtoProperties extends PropertyAccess<BaseDto> {
  
  public final ModelKeyProvider<BaseDto> key = new ModelKeyProvider<BaseDto>() {
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  };
  
  
  ValueProvider<BaseDto, String> name();
  
  public final ValueProvider<BaseDto, String> author = new ValueProvider<BaseDto, String>() {
    public String getValue(BaseDto object) {
      return object instanceof MusicDto ? ((MusicDto) object).getAuthor() : "";
    }
    public void setValue(BaseDto object, String value) {
      if (object instanceof MusicDto) {
        ((MusicDto) object).setAuthor(value);
      }
    }
    public String getPath() {
      return "author";
    }
  };
  
  public final ValueProvider<BaseDto, String> genre = new ValueProvider<BaseDto, String>() {
    public String getValue(BaseDto object) {
      return object instanceof MusicDto ? ((MusicDto) object).getGenre() : "";
    }
    public void setValue(BaseDto object, String value) {
      if (object instanceof MusicDto) {
        ((MusicDto) object).setGenre(value);
      }
    }
    public String getPath() {
      return "genre";
    }
  };
}
