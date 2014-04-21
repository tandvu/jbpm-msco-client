/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.msco.mil.client.sencha.gxt.explorer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.sencha.gxt.core.client.GXT;
import com.msco.mil.client.sencha.gxt.explorer.client.app.ioc.ExplorerGinjector;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.StateManager;

public class Explorer implements EntryPoint {

  private final ExplorerGinjector injector = GWT.create(ExplorerGinjector.class);

  public void onModuleLoad() {
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      public void execute() {
        StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));

        ExplorerApp app = injector.getApp();
        app.run();

        onReady();
      }

    });
  }

  private native void onReady() /*-{
		if (typeof $wnd.GxtReady != 'undefined') {
			$wnd.GxtReady();
		}
  }-*/;

}
