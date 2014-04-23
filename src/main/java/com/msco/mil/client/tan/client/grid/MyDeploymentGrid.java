package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class MyDeploymentGrid extends ContentPanel {
	private static final MyDeploymentProperties myDeploymentProps = GWT
			.create(MyDeploymentProperties.class);
	private Grid<MyDeployment> myDeploymentGrid;

	public MyDeploymentGrid() {        
		// Create columns
		ColumnConfig<MyDeployment, String> groupIdCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.groupId(), 150, "Group ID");
		ColumnConfig<MyDeployment, String> artifactIdCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.artifactId(), 150, "Artifact");
		ColumnConfig<MyDeployment, String> versionCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.version(), 150, "Version");
		ColumnConfig<MyDeployment, String> kbaseNameCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.kbaseName(), 150, "Kie Base Name");
		ColumnConfig<MyDeployment, String> ksessionNameCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.ksessionName(), 150, "Kie Session Name");
		ColumnConfig<MyDeployment, String> strategyNameCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.strategy(), 150, "Strategy");
		ColumnConfig<MyDeployment, String> statusCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.status(), 150, "Action");
		ColumnConfig<MyDeployment, String> identifierCol = new ColumnConfig<MyDeployment, String>(
				myDeploymentProps.identifier(), 250, "MyDeployments");

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

		// Generate listStore (data)
		ListStore<MyDeployment> listStore = new ListStore<MyDeployment>(
				myDeploymentProps.key());
		// listStore.addAll(TestData.getFriends());

		// Build Grid (listStore, columnModel)

		myDeploymentGrid = new Grid<MyDeployment>(listStore, colModel);
		// grid.getView().setAutoExpandColumn(nameCol);
		myDeploymentGrid.setBorders(true);
		myDeploymentGrid.getView().setStripeRows(true);
		myDeploymentGrid.getView().setColumnLines(true);
		this.add(myDeploymentGrid);
		this.setHeaderVisible(false);
	}
}
