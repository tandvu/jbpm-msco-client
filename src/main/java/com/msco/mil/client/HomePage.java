package com.msco.mil.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.msco.mil.client.tan.client.JsonGridExample;
import com.msco.mil.client.tan.client.PortalLayoutPage;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HomePage implements IsWidget, EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		System.out.println("HomePage.onModuleLoad()");
		RootPanel.get().add(new PortalLayoutPage());
//		RootPanel.get().add(new JsonGridExample());
	}

	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}
}
