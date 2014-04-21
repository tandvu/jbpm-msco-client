/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.msco.mil.client.sencha.gxt.explorer.client.app.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ExamplePlace extends Place {

  public static class Tokenizer implements PlaceTokenizer<ExamplePlace> {

    public ExamplePlace getPlace(String token) {
      return new ExamplePlace(token);
    }

    public String getToken(ExamplePlace place) {
      return place.getExampleId();
    }

  }

  private String exampleId;

  public ExamplePlace(String exampleId) {
    this.exampleId = exampleId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ExamplePlace) {
      return exampleId.equals(((ExamplePlace) obj).exampleId);
    }
    return false;
  }

  public String getExampleId() {
    return exampleId;
  }

  @Override
  public int hashCode() {
    return exampleId.hashCode();
  }
}
