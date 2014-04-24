package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.msco.mil.client.tan.client.RestEngineService;
import com.msco.mil.client.tan.client.RestEngineServiceAsync;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class MyDeploymentGrid extends ContentPanel {
	private static final MyDeploymentProperties props = GWT
			.create(MyDeploymentProperties.class);
	private RestEngineServiceAsync restEngineService =  
			GWT.create(RestEngineService.class);
	private Grid<MyDeployment> grid;

	public MyDeploymentGrid() {
		// Create columns
		ColumnConfig<MyDeployment, String> groupIdCol = new ColumnConfig<MyDeployment, String>(
				props.groupId(), 150, "Group ID");
		ColumnConfig<MyDeployment, String> artifactIdCol = new ColumnConfig<MyDeployment, String>(
				props.artifactId(), 150, "Artifact");
		ColumnConfig<MyDeployment, String> versionCol = new ColumnConfig<MyDeployment, String>(
				props.version(), 150, "Version");
		ColumnConfig<MyDeployment, String> kbaseNameCol = new ColumnConfig<MyDeployment, String>(
				props.kbaseName(), 150, "Kie Base Name");
		ColumnConfig<MyDeployment, String> ksessionNameCol = new ColumnConfig<MyDeployment, String>(
				props.ksessionName(), 150, "Kie Session Name");
		ColumnConfig<MyDeployment, String> strategyNameCol = new ColumnConfig<MyDeployment, String>(
				props.strategy(), 150, "Strategy");
		ColumnConfig<MyDeployment, String> statusCol = new ColumnConfig<MyDeployment, String>(
				props.status(), 150, "Action");
		ColumnConfig<MyDeployment, String> identifierCol = new ColumnConfig<MyDeployment, String>(
				props.identifier(), 250, "MyDeployments");

		// Set kbaseName & ksessionName columns
		kbaseNameCol.setCell(new AbstractCell<String>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
				if (value.equals("")) {
					sb.appendHtmlConstant("<span>" + "DEFAULT" + "</span>");
				}
			}
		});

		ksessionNameCol.setCell(new AbstractCell<String>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
				if (value.equals("")) {
					sb.appendHtmlConstant("<span>" + "DEFAULT" + "</span>");
				}
			}
		});

		// Build Column Model
		List<ColumnConfig<MyDeployment, ?>> colList = new ArrayList<ColumnConfig<MyDeployment, ?>>();
		colList.add(identifierCol);
		colList.add(groupIdCol);
		colList.add(artifactIdCol);
		colList.add(versionCol);
		colList.add(kbaseNameCol);
		colList.add(ksessionNameCol);
		colList.add(strategyNameCol);
		colList.add(statusCol);
		ColumnModel<MyDeployment> colModel = new ColumnModel<MyDeployment>(
				colList);

		ListStore<MyDeployment> listStore = new ListStore<MyDeployment>(
				props.key());

		grid = new Grid<MyDeployment>(listStore, colModel);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		this.add(grid);
		this.setHeaderVisible(false);
	}
	
    public void updateGrid()
    {
        restEngineService.getDeployments(new AsyncCallback<List<MyDeployment>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list.  Make sure jBPM is running.");
            }
            
            public void onSuccess(List<MyDeployment> deployments) {
            	grid.getStore().replaceAll(deployments);
            }
        });
    }  
}
