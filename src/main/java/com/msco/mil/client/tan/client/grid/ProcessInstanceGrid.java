package com.msco.mil.client.tan.client.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.msco.mil.client.tan.client.RestEngineService;
import com.msco.mil.client.tan.client.RestEngineServiceAsync;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.MyProcessInstanceProperties;
import com.msco.mil.shared.util.MscoDefines;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class ProcessInstanceGrid extends ContentPanel {
	private static final MyProcessInstanceProperties props = GWT
			.create(MyProcessInstanceProperties.class);
	private Grid<MyProcessInstance> grid;
	private RestEngineServiceAsync restEngineService =  
			GWT.create(RestEngineService.class);

	public ProcessInstanceGrid() {
		// Create columns
        ColumnConfig<MyProcessInstance, Long> idCol = new ColumnConfig<MyProcessInstance, Long>(
                props.id(), 30, "Id");
        ColumnConfig<MyProcessInstance, String> nameCol = new ColumnConfig<MyProcessInstance, String>(
                props.name(), 100, "Name");
        ColumnConfig<MyProcessInstance, String> externalIdCol = new ColumnConfig<MyProcessInstance, String>(
                props.externalId(), 220, "Deployment");
        ColumnConfig<MyProcessInstance, String> initiatorCol = new ColumnConfig<MyProcessInstance, String>(
                props.initiator(), 50, "Initiator");
        ColumnConfig<MyProcessInstance, String> versionCol = new ColumnConfig<MyProcessInstance, String>(
                props.version(), 50, "Version");
        
        ColumnConfig<MyProcessInstance, String> stateCol = new ColumnConfig<MyProcessInstance, String>(
                props.state(), 80, "State");
        
        ColumnConfig<MyProcessInstance, Date> dateCol = new ColumnConfig<MyProcessInstance, Date>(
                props.date(), 150, "Start Date");
        dateCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy kk:mm")));

		// Build Column Model
        List<ColumnConfig<MyProcessInstance, ?>> colList = new ArrayList<ColumnConfig<MyProcessInstance, ?>>();
        colList.add(idCol);
        colList.add(nameCol);
        colList.add(externalIdCol);
        colList.add(initiatorCol);
        colList.add(versionCol);
        colList.add(stateCol);
        colList.add(dateCol);
		ColumnModel<MyProcessInstance> colModel = new ColumnModel<MyProcessInstance>(colList);

		ListStore<MyProcessInstance> listStore = new ListStore<MyProcessInstance>(props.key());

		grid = new Grid<MyProcessInstance>(listStore, colModel);
		grid.setBorders(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		
        ToggleGroup group = new ToggleGroup();
        ToggleButton activeTB = new ToggleButton("Active");
        final ToolBar toolBarLowerBottom = new ToolBar();
        activeTB.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
                updateGrid(MscoDefines.ACTIVE_INSTANCE);
            }
        });
        activeTB.setValue(true);
        ToggleButton completedTB = new ToggleButton("Completed");
        completedTB.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
            	updateGrid(MscoDefines.COMPLETED_INSTANCE);
            }
        });
        group.add(activeTB);
        group.add(completedTB);
        toolBarLowerBottom.add(activeTB);
        toolBarLowerBottom.add(completedTB);
        
        VerticalLayoutContainer verticalContainer = new VerticalLayoutContainer();
        verticalContainer.add(grid, new VerticalLayoutData(1, 1));
        verticalContainer.add(toolBarLowerBottom);
		
		
		this.add(verticalContainer);
		this.setHeaderVisible(false);
	}

	public void updateGrid(int status) {
		restEngineService.getProcessInstances(status, 
        		new AsyncCallback<List<MyProcessInstance>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list.  Make sure jBPM is running.");
            }
            
            public void onSuccess(List<MyProcessInstance> processInstances) {
            	grid.getStore().replaceAll(processInstances);
            }
        });
	}
}
