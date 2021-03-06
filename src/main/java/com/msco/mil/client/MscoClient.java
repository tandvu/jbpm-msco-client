package com.msco.mil.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.msco.mil.shared.Actor;
import com.msco.mil.shared.ActorProperties;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.msco.mil.shared.ProcessDefinition;
import com.msco.mil.shared.ProcessDefinitionProperties;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.MyProcessInstanceProperties;
import com.msco.mil.shared.Task;
import com.msco.mil.shared.TaskProperties;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.CellSelectionEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MscoClient implements IsWidget, EntryPoint {
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private Viewport viewport;
    private BorderLayoutContainer conMain = new BorderLayoutContainer();
    private Widget widgetDeployment;
    private Widget widgetProcessInstance;
    private Widget widgetTaskSummary;
    private Widget widgetProcessDefinition;
    private Widget widgetActors;
    private static final MyDeploymentProperties propsDeployment = GWT.create(MyDeploymentProperties.class);
    private static final MyProcessInstanceProperties propsProcessInstance = GWT.create(MyProcessInstanceProperties.class);
    private static final TaskProperties propsTaskSummary = GWT.create(TaskProperties.class);
    private static final ProcessDefinitionProperties propsProcessDefinition = GWT
            .create(ProcessDefinitionProperties.class);
    private static final ActorProperties propsActor = GWT.create(ActorProperties.class);
    private Grid<MyProcessInstance> gridProcessInstance = null;
    private Grid<MyDeployment> gridDeployments = null;
    private Grid<Task> gridTaskSummary = null;
    private Grid<ProcessDefinition> gridProcessDefinition = null;
    private Grid<Actor> gridActor = null;
    private List<MyProcessInstance> activeProcessInstanceList = new ArrayList<MyProcessInstance>();
    private List<MyProcessInstance> completedProcessInstanceList = new ArrayList<MyProcessInstance>();
    private List<Task> taskList = new ArrayList<Task>();
    private List<ProcessDefinition> processDefinitionList = new ArrayList<ProcessDefinition>();
    private List<Actor> actorList = new ArrayList<Actor>();
    private int currentProcessInstanceGrid = 0;
    private ListStore<Actor> store = null;
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final RootLayoutPanel rootPanel = RootLayoutPanel.get();
        viewport = new Viewport();
        viewport.add(asWidget());
        rootPanel.add(viewport);
    }
    
    public Widget deploymentList() {
        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setHeadingText("Deployment Units");
        contentPanel.addStyleName("margin-10");
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        contentPanel.setWidget(con);
        ColumnConfig<MyDeployment, String> identifierCol = new ColumnConfig<MyDeployment, String>(
                propsDeployment.identifier(), 250, "Deployment");
        ColumnConfig<MyDeployment, String> groupIdCol = new ColumnConfig<MyDeployment, String>(propsDeployment.groupId(),
                150, "Group ID");
        ColumnConfig<MyDeployment, String> artifactIdCol = new ColumnConfig<MyDeployment, String>(
                propsDeployment.artifactId(), 150, "Artifact");
        ColumnConfig<MyDeployment, String> versionCol = new ColumnConfig<MyDeployment, String>(propsDeployment.version(),
                150, "Version");
        ColumnConfig<MyDeployment, String> kbaseNameCol = new ColumnConfig<MyDeployment, String>(
                propsDeployment.kbaseName(), 150, "Kie Base Name");
        ColumnConfig<MyDeployment, String> ksessionNameCol = new ColumnConfig<MyDeployment, String>(
                propsDeployment.ksessionName(), 150, "Kie Session Name");
        ColumnConfig<MyDeployment, String> strategyNameCol = new ColumnConfig<MyDeployment, String>(
                propsDeployment.strategy(), 150, "Strategy");
        ColumnConfig<MyDeployment, String> statusCol = new ColumnConfig<MyDeployment, String>(propsDeployment.status(),
                150, "Action");
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
        TextButtonCell buttonUnDeploy = new TextButtonCell() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                SafeHtml html = SafeHtmlUtils
                        .fromTrustedString("<input type='button' style='font-size:100%; margin-top:0%' value=\""
                                + "UnDeploy" + "\" qtip='Center On Map'></input>");
                sb.append(html);
            }
        };
        buttonUnDeploy.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
                Context c = event.getContext();
                int row = c.getIndex();
                MyDeployment d = gridDeployments.getStore().get(row);
                greetingService.undeploy(d.getIdentifier(), new AsyncCallback<String>() {
                    public void onFailure(Throwable caught) {
                        Info.display("Alert", "Network problem getting deployments list");
                    }
                    
                    public void onSuccess(String deployments) {
                        Info.display("Info", deployments);
                    }
                });
            }
        });
        statusCol.setCell((Cell<String>) buttonUnDeploy);
        List<ColumnConfig<MyDeployment, ?>> l = new ArrayList<ColumnConfig<MyDeployment, ?>>();
        l.add(identifierCol);
        l.add(groupIdCol);
        l.add(artifactIdCol);
        l.add(versionCol);
        l.add(kbaseNameCol);
        l.add(ksessionNameCol);
        l.add(strategyNameCol);
        l.add(statusCol);
        ColumnModel<MyDeployment> cm = new ColumnModel<MyDeployment>(l);
        ListStore<MyDeployment> store = new ListStore<MyDeployment>(propsDeployment.key());
        gridDeployments = new Grid<MyDeployment>(store, cm);
        gridDeployments.getView().setStripeRows(true);
        gridDeployments.getView().setColumnLines(true);
        gridDeployments.setBorders(false);
        gridDeployments.setColumnReordering(true);
        con.add(gridDeployments, new VerticalLayoutData(1, 1));
        return contentPanel;
    }
    
    public Widget processInstanceList() {
        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setHeadingText("Process Instances");
        contentPanel.addStyleName("margin-10");
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        contentPanel.setWidget(con);
        ColumnConfig<MyProcessInstance, Long> idCol = new ColumnConfig<MyProcessInstance, Long>(
                propsProcessInstance.id(), 30, "Id");
        ColumnConfig<MyProcessInstance, String> nameCol = new ColumnConfig<MyProcessInstance, String>(
                propsProcessInstance.name(), 100, "Name");
        ColumnConfig<MyProcessInstance, String> externalIdCol = new ColumnConfig<MyProcessInstance, String>(
                propsProcessInstance.externalId(), 220, "Deployment");
        ColumnConfig<MyProcessInstance, String> initiatorCol = new ColumnConfig<MyProcessInstance, String>(
                propsProcessInstance.initiator(), 50, "Initiator");
        ColumnConfig<MyProcessInstance, String> versionCol = new ColumnConfig<MyProcessInstance, String>(
                propsProcessInstance.version(), 50, "Version");
        
        ColumnConfig<MyProcessInstance, String> stateCol = new ColumnConfig<MyProcessInstance, String>(
                propsProcessInstance.state(), 80, "State");
        
        ColumnConfig<MyProcessInstance, Date> dateCol = new ColumnConfig<MyProcessInstance, Date>(
                propsProcessInstance.date(), 150, "Start Date");
        dateCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy kk:mm")));
        initiatorCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                String style = "style='color: black'";
                for (Actor actor : actorList) {
                    if (actor.getName().equals(value)) {
                        style = "style='color: " + actor.getColor() + "'";
                        break;
                    }
                }
                sb.appendHtmlConstant("<span " + style + ">" + value + "</span>");
            }
        });
        List<ColumnConfig<MyProcessInstance, ?>> l = new ArrayList<ColumnConfig<MyProcessInstance, ?>>();
        l.add(idCol);
        l.add(nameCol);
        l.add(externalIdCol);
        l.add(initiatorCol);
        l.add(versionCol);
        l.add(stateCol);
        l.add(dateCol);
        ColumnModel<MyProcessInstance> cm = new ColumnModel<MyProcessInstance>(l);
        ListStore<MyProcessInstance> store = new ListStore<MyProcessInstance>(propsProcessInstance.key());
        gridProcessInstance = new Grid<MyProcessInstance>(store, cm);
        gridProcessInstance.getView().setStripeRows(true);
        gridProcessInstance.getView().setColumnLines(true);
        gridProcessInstance.setBorders(false);
        gridProcessInstance.setColumnReordering(true);
        ToggleGroup group = new ToggleGroup();
        ToggleButton activeTB = new ToggleButton("Active");
        final ToolBar toolBarLowerBottom = new ToolBar();
        activeTB.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
                currentProcessInstanceGrid = 0;
                gridProcessInstance.getStore().replaceAll(activeProcessInstanceList);
            }
        });
        activeTB.setValue(true);
        ToggleButton completedTB = new ToggleButton("Completed");
        completedTB.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
                currentProcessInstanceGrid = 1;
                gridProcessInstance.getStore().replaceAll(completedProcessInstanceList);
            }
        });
        group.add(activeTB);
        group.add(completedTB);
        toolBarLowerBottom.add(activeTB);
        toolBarLowerBottom.add(completedTB);
        con.add(gridProcessInstance, new VerticalLayoutData(1, 1));
        con.add(toolBarLowerBottom);
        return contentPanel;
    }
    
    public Widget processDefinitionList() {
        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setHeadingText("Process Definitions");
        contentPanel.addStyleName("margin-10");
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        contentPanel.setWidget(con);
        ColumnConfig<ProcessDefinition, Long> idCol = new ColumnConfig<ProcessDefinition, Long>(
                propsProcessDefinition.id(), 50, "Id");
        ColumnConfig<ProcessDefinition, String> nameCol = new ColumnConfig<ProcessDefinition, String>(
                propsProcessDefinition.name(), 50, "Name");
        ColumnConfig<ProcessDefinition, String> initiatorCol = new ColumnConfig<ProcessDefinition, String>(
                propsProcessDefinition.initiator(), 50, "Initiator");
        ColumnConfig<ProcessDefinition, Date> dateCol = new ColumnConfig<ProcessDefinition, Date>(
                propsProcessDefinition.date(), 100, "Start Date");
        dateCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy kk:mm")));
        List<ColumnConfig<ProcessDefinition, ?>> l = new ArrayList<ColumnConfig<ProcessDefinition, ?>>();
        l.add(idCol);
        l.add(nameCol);
        l.add(initiatorCol);
        l.add(dateCol);
        ColumnModel<ProcessDefinition> cm = new ColumnModel<ProcessDefinition>(l);
        ListStore<ProcessDefinition> store = new ListStore<ProcessDefinition>(propsProcessDefinition.key());
        gridProcessDefinition = new Grid<ProcessDefinition>(store, cm);
        gridProcessDefinition.getView().setStripeRows(true);
        gridProcessDefinition.getView().setColumnLines(true);
        gridProcessDefinition.setBorders(false);
        gridProcessDefinition.setColumnReordering(true);
        con.add(gridProcessDefinition, new VerticalLayoutData(1, 1));
        return contentPanel;
    }
    
    public Widget taskList() {
        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setHeadingText("Task Summary");
        contentPanel.addStyleName("margin-10");
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        contentPanel.setWidget(con);
        ColumnConfig<Task, Long> idCol = new ColumnConfig<Task, Long>(propsTaskSummary.id(), 50, "Id");
        ColumnConfig<Task, String> nameCol = new ColumnConfig<Task, String>(propsTaskSummary.name(), 150, "Task");
        ColumnConfig<Task, Integer> priorityCol = new ColumnConfig<Task, Integer>(propsTaskSummary.priority(), 50,
                "Priority");
        ColumnConfig<Task, String> statusCol = new ColumnConfig<Task, String>(propsTaskSummary.status(), 100, "Status");
        ColumnConfig<Task, Date> createdOnCol = new ColumnConfig<Task, Date>(propsTaskSummary.createdOn(), 150,
                "Created On");
        createdOnCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy kk:mm")));
        ColumnConfig<Task, Date> dueOnCol = new ColumnConfig<Task, Date>(propsTaskSummary.expiration(), 150, "Due On");
        dueOnCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy kk:mm")));
        ColumnConfig<Task, String> ownerCol = new ColumnConfig<Task, String>(propsTaskSummary.owner(), 100, "Owner");
        ColumnConfig<Task, String> parentCol = new ColumnConfig<Task, String>(propsTaskSummary.parentName(), 100,
                "Process Instance");
        ColumnConfig<Task, String> deploymentCol = new ColumnConfig<Task, String>(propsTaskSummary.deployment(), 220,
                "Deployment");
        ColumnConfig<Task, String> action1Col = new ColumnConfig<Task, String>(propsTaskSummary.action1(), 50, "Action");
        ownerCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                String style = "style='color: black'";
                for (Actor actor : actorList) {
                    if (actor.getName().equals(value)) {
                        style = "style='color: " + actor.getColor() + "'";
                        break;
                    }
                }
                sb.appendHtmlConstant("<span " + style + ">" + value + "</span>");
            }
        });
        TextButtonCell buttonSkip = new TextButtonCell() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                SafeHtml html = SafeHtmlUtils
                        .fromTrustedString("<input type='button' style='font-size:100%; margin-top:0%' value=\""
                                + "Skip" + "\" qtip='Center On Map'></input>");
                sb.append(html);
            }
        };
        buttonSkip.addSelectHandler(new SelectHandler() {
            public void onSelect(SelectEvent event) {
                Context c = event.getContext();
                int row = c.getIndex();
                Task t = gridTaskSummary.getStore().get(row);
                /*
                 * greetingService.undeploy(t.getIdentifier(), new
                 * AsyncCallback<String>() { public void onFailure(Throwable
                 * caught) { Info.display("Alert",
                 * "Network problem getting deployments list"); } public void
                 * onSuccess(String deployments) { Info.display("Info",
                 * deployments); } });
                 */
            }
        });
        action1Col.setCell((Cell<String>) buttonSkip);
        List<ColumnConfig<Task, ?>> l = new ArrayList<ColumnConfig<Task, ?>>();
        l.add(idCol);
        l.add(nameCol);
        l.add(priorityCol);
        l.add(statusCol);
        l.add(createdOnCol);
        l.add(dueOnCol);
        l.add(ownerCol);
        l.add(parentCol);
        l.add(deploymentCol);
        l.add(action1Col);
        ColumnModel<Task> cm = new ColumnModel<Task>(l);
        ListStore<Task> store = new ListStore<Task>(propsTaskSummary.key());
        gridTaskSummary = new Grid<Task>(store, cm);
        gridTaskSummary.getView().setStripeRows(true);
        gridTaskSummary.getView().setColumnLines(true);
        gridTaskSummary.setBorders(false);
        gridTaskSummary.setColumnReordering(true);
        con.add(gridTaskSummary, new VerticalLayoutData(1, 1));
        return contentPanel;
    }
    
    public Widget actorList() {
        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setHeadingText("Actors");
        contentPanel.addStyleName("margin-10");
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        contentPanel.setWidget(con);
        ColumnConfig<Actor, String> nameCol = new ColumnConfig<Actor, String>(propsActor.name(), 50, "Name");
        ListStore<String> lights = new ListStore<String>(new ModelKeyProvider<String>() {
            public String getKey(String item) {
                return item;
            }
        });
        nameCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                String style = "style='color: black'";
                for (Actor actor : actorList) {
                    if (actor.getName().equals(value)) {
                        style = "style='color: " + actor.getColor() + "'";
                        break;
                    }
                }
                sb.appendHtmlConstant("<span " + style + ">" + value + "</span>");
            }
        });
        lights.add("black");
        lights.add("red");
        lights.add("blue");
        lights.add("green");
        lights.add("orange");
        SafeStyles fieldPaddingStyle = SafeStylesUtils.fromTrustedString("padding: 2px 3px;");
        ColumnConfig<Actor, String> colorColumn = new ColumnConfig<Actor, String>(propsActor.color(), 100, "Color");
        colorColumn.setColumnTextStyle(fieldPaddingStyle);
        ComboBoxCell<String> colorCombo = new ComboBoxCell<String>(lights, new LabelProvider<String>() {
            public String getLabel(String item) {
                return item;
            }
        });
        colorCombo.addSelectionHandler(new SelectionHandler<String>() {
            public void onSelection(SelectionEvent<String> event) {
                CellSelectionEvent<String> sel = (CellSelectionEvent<String>) event;
                Actor p = store.get(sel.getContext().getIndex());
                for (Actor r : actorList) {
                    if (r.getName().equals(p.getName())) {
                        r.setColor(event.getSelectedItem());
                        gridActor.getView().refresh(false);
                        gridProcessInstance.getView().refresh(false);
                        gridTaskSummary.getView().refresh(false);
                    }
                }
            }
        });
        colorCombo.setTriggerAction(TriggerAction.ALL);
        colorCombo.setForceSelection(true);
        colorColumn.setCell(colorCombo);
        colorCombo.setWidth(90);
        List<ColumnConfig<Actor, ?>> l = new ArrayList<ColumnConfig<Actor, ?>>();
        l.add(nameCol);
        l.add(colorColumn);
        ColumnModel<Actor> cm = new ColumnModel<Actor>(l);
        store = new ListStore<Actor>(propsActor.key());
        gridActor = new Grid<Actor>(store, cm);
        gridActor.getView().setStripeRows(true);
        gridActor.getView().setColumnLines(true);
        gridActor.setBorders(false);
        gridActor.setColumnReordering(true);
        con.add(gridActor, new VerticalLayoutData(1, 1));
        return contentPanel;
    }
    
    public Widget asWidget() {
        BorderLayoutData northData = new BorderLayoutData(150);
        northData.setMargins(new Margins(0, 0, 5, 0));
        northData.setCollapsible(true);
        northData.setCollapseMini(true);
        northData.setSplit(true);
        northData.setMinSize(10);
        BorderLayoutData westData = new BorderLayoutData(170);
        westData.setCollapsible(true);
        westData.setSplit(true);
        westData.setCollapseMini(true);
        westData.setMargins(new Margins(0, 5, 0, 0));
        westData.setMinSize(50);
        final MarginData centerData = new MarginData();
        final BorderLayoutData eastData = new BorderLayoutData(750);
        eastData.setMargins(new Margins(0, 0, 0, 5));
        eastData.setCollapsible(true);
        eastData.setSplit(true);
        eastData.setCollapseMini(true);
        eastData.setMinSize(50);
        BorderLayoutData southData = new BorderLayoutData(250);
        southData.setMargins(new Margins(5, 0, 0, 0));
        southData.setCollapsible(true);
        southData.setCollapseMini(true);
        widgetDeployment = deploymentList();
        conMain.setNorthWidget(widgetDeployment, northData);
        widgetProcessInstance = processInstanceList();
        conMain.setEastWidget(widgetProcessInstance, eastData);
        widgetTaskSummary = taskList();
        conMain.setSouthWidget(widgetTaskSummary, southData);
        widgetProcessDefinition = processDefinitionList();
        conMain.setCenterWidget(widgetProcessDefinition, centerData);
        widgetActors = actorList();
        conMain.setWestWidget(widgetActors, westData);
        /*
         * conMain.setWestWidget(westDirectoryTree(), westData); widgetCenter =
         * centerGooglePlaybackMap(); conMain.setCenterWidget(widgetCenter,
         * centerData); conMain.setEastWidget(eastPlaybackList(), eastData);
         * widgetSouth = southMessageDetailsList();
         * conMain.setSouthWidget(widgetSouth, southData); SimpleContainer
         * simple = new SimpleContainer(); simple.add(conMain, new
         * MarginData(1));
         */
        greetingService.getActors(new AsyncCallback<List<Actor>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list");
            }
            
            public void onSuccess(List<Actor> actors) {
                // System.out.println("actorsize:" + actors.size());
                actorList = actors;
                gridActor.getStore().addAll(actorList);
            }
        });
        updateLists();
        Timer t = new Timer() {
            public void run() {
                updateLists();
            }
        };
        // Schedule the timer to run every 2 seconds
        t.scheduleRepeating(4000);
        return conMain;
    }
    
    public void updateLists() {
        greetingService.getDeployments(new AsyncCallback<List<MyDeployment>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Deployments list");
            }
            
            public void onSuccess(List<MyDeployment> deployments) {
                if (deployments != null) {
                    if (deployments.size() == 0) {
                        gridDeployments.getStore().clear();
                        return;
                    }
                    gridDeployments.getStore().replaceAll(deployments);
                }
            }
        });
        greetingService.getProcessInstances(1, new AsyncCallback<List<MyProcessInstance>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Active Process Instances list");
            }
            
            public void onSuccess(List<MyProcessInstance> processInstances) {
                if (processInstances != null) {
                    if (processInstances.size() == 0) {
                        if (currentProcessInstanceGrid == 0) {
                            gridProcessInstance.getStore().clear();
                        }
                        return;
                    }
                    activeProcessInstanceList = processInstances;
                    if (currentProcessInstanceGrid == 0) {
                        gridProcessInstance.getStore().replaceAll(activeProcessInstanceList);
                    }
                }
            }
        });
        greetingService.getProcessInstances(2, new AsyncCallback<List<MyProcessInstance>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Completed Process Instances list");
            }
            
            public void onSuccess(List<MyProcessInstance> processInstances) {
                if (processInstances != null) {
                    if (processInstances.size() == 0) {
                        if (currentProcessInstanceGrid == 1) {
                            gridProcessInstance.getStore().clear();
                        }
                        return;
                    }
                    completedProcessInstanceList = processInstances;
                    if (currentProcessInstanceGrid == 1) {
                        gridProcessInstance.getStore().replaceAll(completedProcessInstanceList);
                    }
                }
            }
        });
        greetingService.getTasks(new AsyncCallback<List<Task>>() {
            public void onFailure(Throwable caught) {
                Window.alert("Network problem getting Completed Process Instances list");
            }
            
            public void onSuccess(List<Task> taskSummaryList) {
                taskList = taskSummaryList;
                gridTaskSummary.getStore().replaceAll(taskList);
                // System.out.println("taskList size:" + taskList.size());
            }
        });
    }
}
