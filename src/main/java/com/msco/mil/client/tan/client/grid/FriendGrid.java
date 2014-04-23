package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.msco.mil.client.sencha.gxt.examples.resources.client.TestData;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.Friend;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.FriendProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class FriendGrid extends ContentPanel {
	private static final FriendProperties friendProps = GWT
			.create(FriendProperties.class);

	public FriendGrid() {
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

		// Generate listStore (data)
		ListStore<Friend> listStore = new ListStore<Friend>(friendProps.key());
		listStore.addAll(TestData.getFriends());

		// Build Grid (listStore, columnModel)
		final Grid<Friend> grid = new Grid<Friend>(listStore, colModel);
		grid.getView().setAutoExpandColumn(nameCol);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		this.add(grid);
		this.setHeaderVisible(false);
	}
}
