package com.msco.mil.client;

import org.kie.internal.utils.KieHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.msco.mil.client.sencha.gxt.examples.resources.client.AccordionLayoutExample;
import com.msco.mil.client.sencha.gxt.examples.resources.client.PortalLayoutContainerExample;
import com.msco.mil.client.tan.client.TanPortalLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HomePage implements EntryPoint {
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
		RootPanel.get().add(new TanPortalLayout());
//		RootPanel.get().add(new AccordionLayoutExample());
	}

	public class MainGUI extends Composite {
		public MainGUI() {
			VerticalPanel vPanel = new VerticalPanel();
			TextBox txt1;
			Label resultLbl;
			TextBox numTxt1, numTxt2;
			txt1 = new TextBox();

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(txt1);

			numTxt1 = new TextBox();

			numTxt2 = new TextBox();
			hPanel.add(numTxt1);
			hPanel.add(numTxt2);

			DecoratorPanel decPanel = new DecoratorPanel();
			decPanel.setWidget(hPanel);

			vPanel.add(decPanel);

			Button btn1 = new Button("Say Hello");
			vPanel.add(btn1);

			resultLbl = new Label("Result will be here");
			vPanel.add(resultLbl);
			initWidget(vPanel);
		}
	}
}
