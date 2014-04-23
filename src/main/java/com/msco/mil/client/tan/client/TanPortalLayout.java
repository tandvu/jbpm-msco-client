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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.msco.mil.client.GreetingService;
import com.msco.mil.client.sencha.gxt.examples.resources.client.AccordionLayoutExample;
import com.msco.mil.client.sencha.gxt.examples.resources.client.ExampleStyles;
import com.msco.mil.client.sencha.gxt.examples.resources.client.TestData;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.Friend;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.FriendProperties;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.Stock;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.StockProperties;
import com.msco.mil.client.tan.client.grid.MyDeploymentGrid;
import com.msco.mil.shared.Deployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.sencha.gxt.core.client.dom.Layer;
import com.sencha.gxt.data.shared.ListStore;
//import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public class TanPortalLayout implements IsWidget, EntryPoint {

	private static final FriendProperties friendProps = 
			GWT.create(FriendProperties.class);
	private static final StockProperties stockProps = 
			GWT.create(StockProperties.class);
	private static final MyDeploymentProperties deploymentProps = 
			GWT.create(MyDeploymentProperties.class);
	private RestEngineServiceAsync restEngineService =  
			GWT.create(RestEngineService.class);
	
	private Grid<Deployment> deploymentGrid = null;

	private PortalLayoutContainer portal;

	public Widget asWidget() {
		if (portal == null) {
			portal = new PortalLayoutContainer(3);
			portal.getElement().getStyle().setBackgroundColor("white");
			portal.setColumnWidth(0, .20);
			portal.setColumnWidth(1, .50);
			portal.setColumnWidth(2, .30);
//			portal.setShadowPosition(Layer.ShadowPosition.SIDES);
//			portal.setShadow(true);
//			portal.setTitle("This is PortalLayout");
//			portal.setWidth(500);
//			portal.setPixelSize(750, 500);
//			portal.setBorders(true);
			
			Portlet portlet = new Portlet();
			portlet.setHeadingText("Another Panel 3");
			configPanel(portlet);
			portlet.add(new AccordionLayoutExample());
			portlet.setShadow(true);
			portlet.setShadowPosition(Layer.ShadowPosition.SIDES);
			portal.add(portlet, 0);

			portlet = new Portlet();
			portlet.setHeadingText("Grid in a Portlet");
			configPanel(portlet);
			portlet.add(createStockGrid());
			portlet.setHeight(250);
			portal.add(portlet, 1);
			
			
			//Add friend porlet
			portlet = new Portlet();
			portlet.setHeadingText("Deployment Grid in a Portlet");
			configPanel(portlet);
			portlet.add(instantiateDeploymentGrid());
			portlet.setHeight(200);
			portal.add(portlet, 1);

			portlet = new Portlet();
			portlet.setHeadingText("Another Panel 1");
			configPanel(portlet);
			portlet.add(getBogusText());
			portal.add(portlet, 2);

			portlet = new Portlet();
			portlet.setHeadingText("Panel 2");
			configPanel(portlet);
			portlet.add(getBogusText());
			portal.add(portlet, 2);

			portlet = new Portlet();
			portlet.setHeadingText("Another Panel 2");
			configPanel(portlet);
			portlet.add(getBogusText());
			portal.add(portlet, 2);
		}
		
		updateLists();
		return portal;
	}
	
	public Widget instantiateDeploymentGrid() {
		// Create columns
        ColumnConfig<Deployment, String> groupIdCol = new ColumnConfig<Deployment, String>(deploymentProps.groupId(),
                150, "Group ID");
        ColumnConfig<Deployment, String> artifactIdCol = new ColumnConfig<Deployment, String>(
                deploymentProps.artifactId(), 150, "Artifact");
        ColumnConfig<Deployment, String> versionCol = new ColumnConfig<Deployment, String>(deploymentProps.version(),
                150, "Version");
        ColumnConfig<Deployment, String> kbaseNameCol = new ColumnConfig<Deployment, String>(
                deploymentProps.kbaseName(), 150, "Kie Base Name");
        ColumnConfig<Deployment, String> ksessionNameCol = new ColumnConfig<Deployment, String>(
                deploymentProps.ksessionName(), 150, "Kie Session Name");
        ColumnConfig<Deployment, String> strategyNameCol = new ColumnConfig<Deployment, String>(
                deploymentProps.strategy(), 150, "Strategy");
        ColumnConfig<Deployment, String> statusCol = new ColumnConfig<Deployment, String>(deploymentProps.status(),
                150, "Action");
        ColumnConfig<Deployment, String> identifierCol = new ColumnConfig<Deployment, String>(
        		deploymentProps.identifier(), 250, "Deployments");

		// Set kbaseName & ksessionName columns
        kbaseNameCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (value.equals("")) {
                    sb.appendHtmlConstant("<span>" + "DEFAULT" + "</span>");
                }
            }
        });
        
        
        ksessionNameCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (value.equals("")) {
                    sb.appendHtmlConstant("<span>" + "DEFAULT" + "</span>");
                }
            }
        });

		// Build Column Model
        List<ColumnConfig<Deployment, ?>> colList = new ArrayList<ColumnConfig<Deployment, ?>>();
        colList.add(identifierCol);
        colList.add(groupIdCol);
        colList.add(artifactIdCol);
        colList.add(versionCol);
        colList.add(kbaseNameCol);
        colList.add(ksessionNameCol);
        colList.add(strategyNameCol);
        colList.add(statusCol);
        ColumnModel<Deployment> colModel = new ColumnModel<Deployment>(colList);
		
		//Generate listStore (data)
		ListStore<Deployment> listStore = new ListStore<Deployment>(deploymentProps.key());
//		listStore.addAll(TestData.getFriends());
		
		//Build Grid (listStore, columnModel)
		deploymentGrid = new Grid<Deployment>(listStore, colModel);
//		grid.getView().setAutoExpandColumn(nameCol);
		deploymentGrid.setBorders(true);
		deploymentGrid.getView().setStripeRows(true);
		deploymentGrid.getView().setColumnLines(true);

		// needed to enable quicktips (qtitle for the heading and qtip for the
		// content) that are setup in the change GridCellRenderer
		QuickTip toolTip = new QuickTip(deploymentGrid);

		return deploymentGrid;
	}
	
    public void updateLists() {
    	System.out.println("TanPortalLayout.updateList");
        restEngineService.getDeployments(new AsyncCallback<List<Deployment>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list");
            }
            
            public void onSuccess(List<Deployment> deployments) {
                if (deployments != null) {
                    if (deployments.size() == 0) {
                        deploymentGrid.getStore().clear();
                        return;
                    }
                    
                    if (deploymentGrid != null)
                    {
                    	deploymentGrid.getStore().replaceAll(deployments);
                    }
                }
            }
        });
    }
	
	public Widget createFriendGrid() {
		// Create columns
		ColumnConfig<Friend, String> nameCol = new ColumnConfig<Friend, String>(
				friendProps.name(), 200, "Name");
		ColumnConfig<Friend, Integer> ageCol = new ColumnConfig<Friend, Integer>(
				friendProps.age(), 200, "Age");
		ColumnConfig<Friend, Boolean> isMaleCol = new ColumnConfig<Friend, Boolean>(
				friendProps.isMale(), 200, "Gender");

		// Set male/female based on boolean isMale
		isMaleCol.setCell(new AbstractCell<Boolean>() {
			@Override
			public void render(Context context, Boolean value,
					SafeHtmlBuilder sb) {
				if (value) {
					sb.appendHtmlConstant("<span>" + "MALE" + "</span>");
				} else {
					sb.appendHtmlConstant("<span>" + "FEMALE" + "</span>");
				}
			}
		});

		// Build Column Model
		List<ColumnConfig<Friend, ?>> colList = new ArrayList<ColumnConfig<Friend, ?>>();
		colList.add(nameCol);
		colList.add(ageCol);
		colList.add(isMaleCol);
		ColumnModel<Friend> colModel = new ColumnModel<Friend>(colList);
		
		//Generate listStore (data)
		ListStore<Friend> listStore = new ListStore<Friend>(friendProps.key());
		listStore.addAll(TestData.getFriends());
		
		//Build Grid (listStore, columnModel)
		final Grid<Friend> grid = new Grid<Friend>(listStore, colModel);
		grid.getView().setAutoExpandColumn(nameCol);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		// needed to enable quicktips (qtitle for the heading and qtip for the
		// content) that are setup in the change GridCellRenderer
		QuickTip toolTip = new QuickTip(grid);

		return grid;
	}

	public Widget createStockGrid() {
		final NumberFormat number = NumberFormat.getFormat("0.00");

		ColumnConfig<Stock, String> nameCol = new ColumnConfig<Stock, String>(
				stockProps.name(), 200, "Company");
		ColumnConfig<Stock, String> symbolCol = new ColumnConfig<Stock, String>(
				stockProps.symbol(), 100, "Symbol");
		ColumnConfig<Stock, Double> lastCol = new ColumnConfig<Stock, Double>(
				stockProps.last(), 75, "Last");

		ColumnConfig<Stock, Double> changeCol = new ColumnConfig<Stock, Double>(
				stockProps.change(), 100, "Change");
		changeCol.setCell(new AbstractCell<Double>() {

			@Override
			public void render(Context context, Double value, SafeHtmlBuilder sb) {
				String style = "style='color: " + (value < 0 ? "red" : "green")
						+ "'";
				String v = number.format(value);
				sb.appendHtmlConstant("<span " + style
						+ " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
			}
		});

		ColumnConfig<Stock, Date> lastTransCol = new ColumnConfig<Stock, Date>(
				stockProps.lastTrans(), 100, "Last Updated");
		lastTransCol.setCell(new DateCell(DateTimeFormat
				.getFormat("MM/dd/yyyy")));

		List<ColumnConfig<Stock, ?>> l = new ArrayList<ColumnConfig<Stock, ?>>();
		l.add(nameCol);
		l.add(symbolCol);
		l.add(lastCol);
		l.add(changeCol);
		l.add(lastTransCol);
		ColumnModel<Stock> cm = new ColumnModel<Stock>(l);

		ListStore<Stock> store = new ListStore<Stock>(stockProps.key());
		store.addAll(TestData.getStocks());

		final Grid<Stock> grid = new Grid<Stock>(store, cm);
		grid.getView().setAutoExpandColumn(nameCol);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		// needed to enable quicktips (qtitle for the heading and qtip for the
		// content) that are setup in the change GridCellRenderer
		new QuickTip(grid);

		return grid;
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

	private HTML getBogusText() {
		HTML html = new HTML(TestData.DUMMY_TEXT_SHORT);
		html.addStyleName(ExampleStyles.get().paddedText());
		return html;
	}

}
