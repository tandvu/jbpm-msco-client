package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.msco.mil.client.tan.client.util.MscoClientDefines;
import com.msco.mil.shared.Stock;
import com.msco.mil.shared.StockProperties;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class StockGrid extends ContentPanel {
	private static final StockProperties stockProps = GWT
			.create(StockProperties.class);

	public StockGrid() {
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

		List<ColumnConfig<Stock, ?>> storeList = new ArrayList<ColumnConfig<Stock, ?>>();
		storeList.add(nameCol);
		storeList.add(symbolCol);
		storeList.add(lastCol);
		storeList.add(changeCol);
		storeList.add(lastTransCol);
		ColumnModel<Stock> cm = new ColumnModel<Stock>(storeList);

		ListStore<Stock> store = new ListStore<Stock>(stockProps.key());
		store.addAll(MscoClientDefines.getStocks());

		final Grid<Stock> grid = new Grid<Stock>(store, cm);
		grid.getView().setAutoExpandColumn(nameCol);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		this.add(grid);
		this.setHeaderVisible(false);
	}
}
