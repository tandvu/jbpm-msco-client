/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.msco.mil.client.sencha.gxt.explorer.client.app.ioc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.msco.mil.client.sencha.gxt.explorer.client.ExplorerApp;

@GinModules(ExplorerModule.class)
public interface ExplorerGinjector extends Ginjector {

  ExplorerApp getApp();
}
