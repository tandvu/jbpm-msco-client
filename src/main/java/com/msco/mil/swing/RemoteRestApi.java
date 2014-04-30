package com.msco.mil.swing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.definition.process.Process;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

import com.msco.mil.server.RestEngineServiceImpl;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.util.MscoDefines;

public class RemoteRestApi {
	private String deploymentId = "org.jbpm:Evaluation:1.0";
	private URL baseUrl = null;
	private String user = "krisv";
	private String password = "krisv";
	private RemoteRestRuntimeFactory restSessionFactory;
	private RemoteRuntimeEngine engine;
	private KieSession ksession;
	private TaskService taskService;
	private List<TaskSummary> tasks;

	public RemoteRestApi() {
		try {
			init();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() throws MalformedURLException {
		baseUrl = new URL("http://localhost:8080/jbpm-console");
		restSessionFactory = new RemoteRestRuntimeFactory(deploymentId,
				baseUrl, user, password);
		engine = restSessionFactory.newRuntimeEngine();

		AuditLogService auditLogService = engine.getAuditLogService();

		/*
		 * List<ProcessInstanceLog> processInstanceList =
		 * auditLogService.findProcessInstances(); for (ProcessInstanceLog
		 * processInstanceLog : processInstanceList) {
		 * System.err.println("RemoteRestAPI.processName: " +
		 * processInstanceLog.getProcessName() + " id: " +
		 * processInstanceLog.getId() + " identity: " +
		 * processInstanceLog.getIdentity() + " outcome: " +
		 * processInstanceLog.getOutcome() + " processId: " +
		 * processInstanceLog.getProcessId() + " getProcessVersion: " +
		 * processInstanceLog.getProcessVersion() +
		 * " getParentProcessInstanceId: " +
		 * processInstanceLog.getParentProcessInstanceId() + " status: " +
		 * processInstanceLog.getStatus()); }
		 */

		ksession = engine.getKieSession();

		// start a new process instance
		Map params = new HashMap();
		params.put("employee", "krisv");
		params.put("reason", "Yearly performance evaluation");
		params.put("performance", "Fabulous");
		params.put("notask", "Test");

		// ProcessInstance processInstance = ksession.startProcess("evaluation",
		// params);
		// System.err.println("RemoteRestApi.processInstance.id: " +
		// processInstance.getId());

		RestEngineServiceImpl restEngine = new RestEngineServiceImpl();
		
//		List<MyProcessInstance> processInstanceList = restEngine
//				.getProcessInstances(MscoDefines.ACTIVE_INSTANCE);
//		System.err.println("RemoteRestApi.processInstanceList.size: " + processInstanceList.size());
//		for (MyProcessInstance myProcessInstance : processInstanceList)
//		{
//			System.err.println("RemoteRestApi.myProcessInstance.id: " + myProcessInstance.getId());
//		}

		// VariableScopeInstance variableScope =
		// (VariableScopeInstance) ((org.jbpm.process.instance.ProcessInstance)
		// processInstance)
		// .getContextInstance(VariableScope.VARIABLE_SCOPE);
		// Map<String, Object> variables = variableScope.getVariables();
		// System.err.println("RemoteRestApi.variables: " + variables);

		/*
		 * System.out.println("Process started.  ProcessInstanceId: " +
		 * processInstance.getId());
		 * 
		 * // complete Self Evaluation taskService = engine.getTaskService();
		 * tasks = taskService.getTasksAssignedAsPotentialOwner("krisv",
		 * "en-UK");
		 * 
		 * TaskSummary task = tasks.get(0); for(TaskSummary tasksummarry :
		 * tasks) { if (tasksummarry.getProcessInstanceId() ==
		 * processInstance.getId()) { task = tasksummarry; break; } }
		 * 
		 * System.out.println("'krisv' completing task " + task.getName() + ": "
		 * + task.getDescription()); taskService.start(task.getId(), "krisv");
		 * Map results = new HashMap(); results.put("performance", "exceeding");
		 * taskService.complete(task.getId(), "krisv", results);
		 * 
		 * // john from HR changeUser("john", "john"); task = null;
		 * for(TaskSummary tasksummarry : tasks) { if
		 * (tasksummarry.getProcessInstanceId() == processInstance.getId()) {
		 * task = tasksummarry; break; } }
		 * System.out.println("'john' completing task " + task.getName() + ": "
		 * + task.getDescription());
		 * 
		 * taskService.claim(task.getId(), "john");
		 * taskService.start(task.getId(), "john"); results = new HashMap();
		 * results.put("performance", "acceptable");
		 * taskService.complete(task.getId(), "john", results);
		 * 
		 * // mary from PM changeUser("mary", "mary");
		 * System.err.println("RemoteRestApi.Mary.tasks.size: " + tasks.size());
		 * task = null; for(TaskSummary tasksummarry : tasks) { if
		 * (tasksummarry.getProcessInstanceId() == processInstance.getId()) {
		 * task = tasksummarry; break; } }
		 * 
		 * System.out.println("'mary' completing task " + task.getName() + ": "
		 * + task.getDescription()); taskService.claim(task.getId(), "mary");
		 * taskService.start(task.getId(), "mary"); results = new HashMap();
		 * results.put("performance", "outstanding");
		 * taskService.complete(task.getId(), "mary", results);
		 * 
		 * // assertProcessInstanceCompleted(processInstance.getId(), ksession);
		 * System.out.println("Process instance completed"); }
		 * 
		 * private void changeUser(String user, String password) { this.user =
		 * user; this.password = password; restSessionFactory = new
		 * RemoteRestRuntimeFactory(deploymentId, baseUrl, user, password);
		 * engine = restSessionFactory.newRuntimeEngine(); ksession =
		 * engine.getKieSession(); taskService = engine.getTaskService(); tasks
		 * = taskService.getTasksAssignedAsPotentialOwner(user, "en-UK");
		 */
	}

	public static void main(String[] args) {
		RemoteRestApi remoteRestApi = new RemoteRestApi();

	}

}
