/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.msco.mil.client.sencha.gxt.examples.resources.client.model;

import com.sencha.gxt.core.client.ValueProvider;

public class MapValueProvider implements ValueProvider<ModelItem, Double> {
  private String field;

  public MapValueProvider(String field) {
    this.field = field;
  }

  public String getPath() {
    return field;
  }

  public Double getValue(ModelItem object) {
    return object.get(field);
  }

  public void setValue(ModelItem object, Double value) {
    object.put(field, value);
  }
}
