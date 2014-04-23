package com.msco.mil.client.tan.client;


/**
 package com.msco.mil.client.sencha.gxt.examples.resources.client;

/**
 * Sencha GXT 3.1.0-beta - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.msco.mil.client.tan.client.grid.MyDeploymentGrid;
import com.msco.mil.client.tan.client.grid.StockGrid;
import com.msco.mil.client.tan.client.grid.TextGrid;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyDeploymentProperties;
//import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class PortalLayoutPage implements IsWidget, EntryPoint {

	private static final MyDeploymentProperties deploymentProps = 
			GWT.create(MyDeploymentProperties.class);
	private RestEngineServiceAsync restEngineService =  
			GWT.create(RestEngineService.class);
	
	private MyDeploymentGrid myDeploymentGrid = null;
	

	private PortalLayoutContainer portal;

	public Widget asWidget() {
		System.out.println("PortalLayoutPage.asWidget()");
		if (portal == null) {
			portal = new PortalLayoutContainer(3);
			portal.getElement().getStyle().setBackgroundColor("white");
			portal.setColumnWidth(0, .20);
			portal.setColumnWidth(1, .50);
			portal.setColumnWidth(2, .30);
			
//			Portlet portlet = new Portlet();
//			configPanel(portlet);
//			portlet.add(new AccordionLayoutExample());
//			portlet.setShadowPosition(Layer.ShadowPosition.SIDES);
//			portal.add(portlet, 0);

			Portlet portlet = new Portlet();
			portlet = new Portlet();
			portlet.setHeadingText("Stocks");
			configPanel(portlet);
			portlet.add(new StockGrid());
			portlet.setHeight(250);
			portal.add(portlet, 1);
			
			//Add friend porlet
			portlet = new Portlet();
			portlet.setHeadingText("Deployments");
			configPanel(portlet);
			myDeploymentGrid = new MyDeploymentGrid();
			portlet.add(myDeploymentGrid);
			portlet.setHeight(200);
			portal.add(portlet, 1);
			
			portlet = new Portlet();
			portlet.setHeadingText("Panel 1");
			configPanel(portlet);
			portlet.add(new TextGrid("Test Data"));
			portal.add(portlet, 2);

			portlet = new Portlet();
			portlet.setHeadingText("Panel 2");
			configPanel(portlet);
			portlet.add(new TextGrid("Test Data"));
			portal.add(portlet, 2);

			portlet = new Portlet();
			portlet.setHeadingText("Panel 3");
			configPanel(portlet);
			portlet.add(new TextGrid("Test Data"));
			portal.add(portlet, 2);
		}
		
		updateLists();
		return portal;
	}
		
    public void updateLists() {
        restEngineService.getDeployments(new AsyncCallback<List<MyDeployment>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list.  Make sure jBPM is running.");
            }
            
            public void onSuccess(List<MyDeployment> deployments) {
            	myDeploymentGrid.updateGrid(deployments);
            }
        });
    }

	public void onModuleLoad() {
		RootPanel.get().add(this);
	}

	private void configPanel(final Portlet panel) {
		panel.setCollapsible(true);
		panel.setAnimCollapse(false);
		panel.getHeader().addTool(new ToolButton(ToolButton.GEAR));
		panel.getHeader().addTool(
				new ToolButton(ToolButton.CLOSE, new SelectHandler() {
					public void onSelect(SelectEvent event) {
						panel.removeFromParent();
					}
				}));
	}
}
