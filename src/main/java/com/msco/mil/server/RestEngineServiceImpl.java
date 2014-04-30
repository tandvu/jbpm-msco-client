package com.msco.mil.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.msco.mil.client.tan.client.RestEngineService;
import com.msco.mil.server.util.MscoServerUtils;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.util.MscoDefines;

@SuppressWarnings("serial")
public class RestEngineServiceImpl extends RemoteServiceServlet implements
		RestEngineService {
	private JSONArray currentDeploymentArray = new JSONArray();
	private List<MyDeployment> deploymentList = new ArrayList<MyDeployment>();

	private List<MyProcessInstance> activeProcessInstanceList = new ArrayList<MyProcessInstance>();
	private List<MyProcessInstance> completedProcessInstanceList = new ArrayList<MyProcessInstance>();
	

	public RestEngineServiceImpl() {
		System.out
				.println("Constructor RestEngineServiceImpl====================");

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						/*
						 * // System.out.println("currentDeploymentIncrement:" +
						 * // currentDeploymentIncrement);
						 * processDeploymentList();
						 * processProcessInstanceList(); processTaskList();
						 */

						Thread.sleep(MscoDefines.REFRESH_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public List<MyDeployment> getDeployments() throws IllegalArgumentException {
		String restDeploymentUrl = MscoServerUtils
				.getRestUrlStr(MscoDefines.DEPLOYMENT_REST_URL);
		String jsonResponseStr = MscoServerUtils.getJsonResponseStr(restDeploymentUrl);
		JSONArray deploymentArray = (JSONArray) JSONSerializer
				.toJSON(jsonResponseStr);

		if (!currentDeploymentArray.equals(deploymentArray)) {
			buildDeploymentListFromResponse(jsonResponseStr);
		}

		return deploymentList;
	}

	private void buildDeploymentListFromResponse(String jsonString) {
		try {
			JSONArray deploymentArray = (JSONArray) JSONSerializer
					.toJSON(jsonString);

			if (currentDeploymentArray.equals(deploymentArray)) {
				return;
			}

			currentDeploymentArray = deploymentArray;

			// clear because there might be a previous data
			synchronized (deploymentList) {
				deploymentList.clear();
				for (Object js : currentDeploymentArray) {
					JSONObject json = (JSONObject) js;
					MyDeployment deployment = new MyDeployment();
					deployment.setArtifactId((String) json.get("artifactId"));
					deployment.setGroupId((String) json.get("groupId"));
					deployment.setKbaseName((String) json.get("kbaseName"));
					deployment.setKsessionName((String) json
							.get("ksessionName"));
					deployment.setStatus((String) json.get("status"));
					deployment.setStrategy((String) json.get("strategy"));
					deployment.setVersion((String) json.get("version"));
					deployment.setIdentifier((String) json.get("identifier"));

					deploymentList.add(deployment);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Build both active and complete process instance lists
	private void buildProcessInstanceLists() throws IllegalArgumentException {
		if (deploymentList.size() == 0)
		{
			getDeployments();
		}
		
		activeProcessInstanceList.clear();
		completedProcessInstanceList.clear();
		synchronized (activeProcessInstanceList) {
			for (MyDeployment deployment : deploymentList) {
				String restProcessInstanceUrl = MscoDefines
						.getProcessInstanceRestUrl(deployment.getIdentifier());
				
				String jsonResponseStr = MscoServerUtils.getJsonResponseStr(restProcessInstanceUrl);
				jsonResponseStr = jsonResponseStr.substring(18, jsonResponseStr.length() - 1);
//				System.err.println("RestEngineServiceImpl.processInstande.jsonStr: " + jsonResponseStr);
				
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
						processInstance.setName((String) json
								.get("processName"));
						processInstance.setInitiator((String) json
								.get("identity"));
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
							activeProcessInstanceList.add(processInstance);
							processInstance.setState("Active");
						} else if (state == MscoDefines.COMPLETED_INSTANCE) {
							completedProcessInstanceList.add(processInstance);
							processInstance.setState("Completed");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Return the process instance list to the caller
	public List<MyProcessInstance> getProcessInstances(Integer status)
			throws IllegalArgumentException {
		buildProcessInstanceLists();
		if (status == MscoDefines.ACTIVE_INSTANCE) {
			return activeProcessInstanceList;
		}
		return completedProcessInstanceList;
	}
}
