package com.msco.mil.swing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.drools.core.command.runtime.process.GetProcessIdsCommand;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

import com.msco.mil.server.util.MscoServerUtils;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.util.MscoDefines;

public class SimpleRestClient {
	private List<MyProcessInstance> processInstanceList = new ArrayList<MyProcessInstance>();
	 private String deploymentId = "org.jbpm:Evaluation:1.0";
//	private String deploymentId = "org.jbpm:HR:1.0";
	private String user = MscoDefines.getAuthorization().getUserId();
	private String password = MscoDefines.getAuthorization().getPassword();

	public SimpleRestClient() {
//		 buildProcessInstanceLists();
//		 buildVariableList();
		buildProcessDefinitionList();
//		try {
//			test();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		setupFactory();
	}


	private void setupFactory() {
		// Setup the factory class with the necessarry information to
		// communicate with the REST services
		URL baseUrl = null;
		try {
			baseUrl = new URL(MscoDefines.SERVER + MscoDefines.JBPM_CONSOLE);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(
				deploymentId, baseUrl, user, password);

		// Create KieSession and TaskService instances and use them
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		KieSession ksession = engine.getKieSession();

		ProcessInstance processInstance = ksession.getProcessInstance(1);
		System.err
				.println("SimpleRestClient.processInstance.id: "
						+ processInstance.getId() + " a: "
						+ processInstance.toString());

		TaskService taskService = engine.getTaskService();
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(
				user, "en-UK");
		for (TaskSummary task : tasks) {
			System.err.println("SimpleRestClient.task: "
					+ task.getProcessInstanceId());
		}

		Collection<org.kie.api.runtime.process.ProcessInstance> processInstances = ksession
				.getProcessInstances();
		System.err.println("SimpleRestClient.processInstances.size: "
				+ processInstances.size());

		// for (org.kie.api.runtime.process.ProcessInstance processInstance :
		// processInstances)
		// {
		// System.err.println("SimpleRestClient.processInstance.id: " +
		// processInstance.getId());
		// }

		/*
		 * ProcessInstance processInstance =
		 * ksession.startProcess("com.burns.reactor.maintenance.cycle"); long
		 * procId = processInstance.getId(); String taskUserId = user;
		 * TaskService taskService = engine.getTaskService(); List<TaskSummary>
		 * tasks = taskService.getTasksAssignedAsPotentialOwner(user, "en-UK");
		 * long taskId = -1;
		 * 
		 * for (TaskSummary task : tasks ) { if (task.getProcessInstanceId() ==
		 * procId ) { taskId = task.getId(); } }
		 * 
		 * if (taskId == -1 ) { throw new
		 * IllegalStateException("Unable to find task for " + user +
		 * " in process instance " + procId ); } taskService.start(taskId,
		 * taskUserId);
		 */
	}

	public static void main(String[] args) throws MalformedURLException {
		SimpleRestClient client = new SimpleRestClient();

		/*
		 * String deploymentId = "org.jbpm:Evaluation:1.0"; URL appUrl = new
		 * URL("http://localhost:8080/jbpm-console/"); String user = "krisv";
		 * String password = "krisv"; RemoteRestRuntimeFactory
		 * restSessionFactory = new RemoteRestRuntimeFactory(deploymentId,
		 * appUrl, user, password);
		 * 
		 * RemoteRuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		 * KieSession ksession = engine.getKieSession(); GetProcessIdsCommand
		 * getProcessIdsCommand = new GetProcessIdsCommand();
		 * Collection<ProcessInstance> processInstanceList =
		 * ksession.getProcessInstances();
		 */

		// ksession.execute(getProcessInstancesCommand);
		// List<String> processIds = ksession.execute(getProcessIdsCommand);
		// System.out.println("The list of process ids"+processIds);
		// for(String pid : processIds){
		// System.out.println("Process Id: "+pid);
		// }
	}

	private void buildProcessInstanceLists() throws IllegalArgumentException {
		processInstanceList.clear();
		synchronized (processInstanceList) {
			String restProcessInstanceUrl = MscoDefines
					.getProcessInstanceRestUrl(deploymentId);
			System.err.println("SimpleRestClient.restprocessInstanceUrl: "
					+ restProcessInstanceUrl);
			String jsonResponseStr = MscoServerUtils
					.getJsonResponseStr(restProcessInstanceUrl);
			 jsonResponseStr = jsonResponseStr.substring(18,
			 jsonResponseStr.length() - 1);

			System.err
					.println("SimpleResetClient.jsonResponseStr.ProcessInstance: "
							+ jsonResponseStr);
			JSONArray processInstanceArray = (JSONArray) JSONSerializer
					.toJSON(jsonResponseStr);

			MyProcessInstance processInstance = null;
			try {
				for (Object js : processInstanceArray) {
					JSONObject json = (JSONObject) js;
					processInstance = new MyProcessInstance();
					Integer id = (Integer) json.get("id");
					Long idLong = id.longValue();
					processInstance.setId(idLong);
					processInstance.setName((String) json.get("processName"));
					processInstance.setInitiator((String) json.get("identity"));
					processInstance.setVersion((String) json
							.get("processVersion"));
					processInstance.setStartDate((Long) json.get("start"));
					processInstance.setExternalId((String) json
							.get("externalId"));
					Long date = (Long) json.get("start");
					Integer state = (Integer) json.get("status");
					processInstance.setDate(new Date(date));
					processInstance.setState("Unknown");

					if (state == MscoDefines.ACTIVE_INSTANCE) {
						processInstanceList.add(processInstance);
						processInstance.setState("Active");
					} else if (state == MscoDefines.COMPLETED_INSTANCE) {
						processInstanceList.add(processInstance);
						processInstance.setState("Completed");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void buildVariableList() throws IllegalArgumentException {
		for (MyProcessInstance processInstance : processInstanceList) {
			String restVariableUrl = MscoDefines.getVariableUrl(deploymentId,
					String.valueOf(processInstance.getId()));
			// System.err.println("SimpleRestClient.restVariableUrl: " +
			// restVariableUrl);
			String jsonResponseStr = MscoServerUtils
					.getJsonResponseStr(restVariableUrl);
			// jsonResponseStr = jsonResponseStr.substring(18,
			// jsonResponseStr.length() - 1);
			// JSONArray variableJsonArray = (JSONArray) JSONSerializer
			// .toJSON(jsonResponseStr);
			System.err.println("SimpleRestClient.jsonResponseStr.Variables: "
					+ jsonResponseStr);
		}
	}
	
	private void buildProcessDefinitionList() throws IllegalArgumentException {
			String processDefinitionUrl = MscoDefines.getProcessDefinitionUrl();
			// System.err.println("SimpleRestClient.restVariableUrl: " +
			// restVariableUrl);
			String jsonResponseStr = MscoServerUtils
					.getJsonResponseStr(processDefinitionUrl);
			// jsonResponseStr = jsonResponseStr.substring(18,
			// jsonResponseStr.length() - 1);
			// JSONArray variableJsonArray = (JSONArray) JSONSerializer
			// .toJSON(jsonResponseStr);
			System.err.println("SimpleRestClient.jsonResponseStr.processDefinitions: "
					+ jsonResponseStr);
		
	}
	
	//This is a code from internet but it doesn't work.  JIRA is issued.
    public void test() throws MalformedURLException
    {
        String deploymentId = "org.jbpm:Evaluation:1.0";
        URL appUrl = new URL("http://localhost:8080/jbpm-console/");
        String user = "admin";
        String password = "admin";
        RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(deploymentId, appUrl, user, password);
        RemoteRuntimeEngine engine = restSessionFactory.newRuntimeEngine();    
        KieSession ksession = engine.getKieSession();
        GetProcessIdsCommand getProcessIdsCommand = new GetProcessIdsCommand();
        //ksession.execute(getProcessInstancesCommand);
        List<String> processIds =  ksession.execute(getProcessIdsCommand);
        System.out.println("The list of process ids"+processIds);
        for(String pid : processIds){
            System.out.println("Process Id: "+pid);
        }
    } 
}
