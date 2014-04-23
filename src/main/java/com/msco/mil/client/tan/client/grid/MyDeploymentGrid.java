package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.msco.mil.shared.Deployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class MyDeploymentGrid extends ContentPanel {
	private static final MyDeploymentProperties deploymentProps = GWT
			.create(MyDeploymentProperties.class);
	private Grid<Deployment> deploymentGrid;

	public MyDeploymentGrid() {        
		// Create columns
		ColumnConfig<Deployment, String> groupIdCol = new ColumnConfig<Deployment, String>(
				deploymentProps.groupId(), 150, "Group ID");
		ColumnConfig<Deployment, String> artifactIdCol = new ColumnConfig<Deployment, String>(
				deploymentProps.artifactId(), 150, "Artifact");
		ColumnConfig<Deployment, String> versionCol = new ColumnConfig<Deployment, String>(
				deploymentProps.version(), 150, "Version");
		ColumnConfig<Deployment, String> kbaseNameCol = new ColumnConfig<Deployment, String>(
				deploymentProps.kbaseName(), 150, "Kie Base Name");
		ColumnConfig<Deployment, String> ksessionNameCol = new ColumnConfig<Deployment, String>(
				deploymentProps.ksessionName(), 150, "Kie Session Name");
		ColumnConfig<Deployment, String> strategyNameCol = new ColumnConfig<Deployment, String>(
				deploymentProps.strategy(), 150, "Strategy");
		ColumnConfig<Deployment, String> statusCol = new ColumnConfig<Deployment, String>(
				deploymentProps.status(), 150, "Action");
		ColumnConfig<Deployment, String> identifierCol = new ColumnConfig<Deployment, String>(
				deploymentProps.identifier(), 250, "Deployments");

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
		List<ColumnConfig<Deployment, ?>> colList = new ArrayList<ColumnConfig<Deployment, ?>>();
		colList.add(identifierCol);
		colList.add(groupIdCol);
		colList.add(artifactIdCol);
		colList.add(versionCol);
		colList.add(kbaseNameCol);
		colList.add(ksessionNameCol);
		colList.add(strategyNameCol);
		colList.add(statusCol);
		ColumnModel<Deployment> colModel = new ColumnModel<Deployment>(
				colList);

		// Generate listStore (data)
		ListStore<Deployment> listStore = new ListStore<Deployment>(
				deploymentProps.key());
		// listStore.addAll(TestData.getFriends());

		// Build Grid (listStore, columnModel)

		deploymentGrid = new Grid<Deployment>(listStore, colModel);
		// grid.getView().setAutoExpandColumn(nameCol);
		deploymentGrid.setBorders(true);
		deploymentGrid.getView().setStripeRows(true);
		deploymentGrid.getView().setColumnLines(true);
		this.add(deploymentGrid);
		this.setHeaderVisible(false);
	}
}
