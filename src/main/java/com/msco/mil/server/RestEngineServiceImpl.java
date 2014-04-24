package com.msco.mil.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.conn.HttpHostConnectException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.kie.services.client.api.RestRequestHelper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.msco.mil.client.tan.client.RestEngineService;
import com.msco.mil.server.util.MscoServerUtils;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.ProcessInstance;
import com.msco.mil.shared.util.MscoDefines;

@SuppressWarnings("serial")
public class RestEngineServiceImpl extends RemoteServiceServlet implements
		RestEngineService {
	private JSONArray currentDeploymentArray = new JSONArray();
	private List<MyDeployment> deploymentList = new ArrayList<MyDeployment>();

	private List<ProcessInstance> activeProcessInstanceList = new ArrayList<ProcessInstance>();
	private List<ProcessInstance> completedProcessInstanceList = new ArrayList<ProcessInstance>();

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

	private String getJsonResponseStr(String restUrl) {
		String jsonResponseStr = "";

		try {
			// create client response in json
			ClientResponse<String> response = MscoServerUtils
					.getGetClientResponseInJason(restUrl);

			// build deployment list from json response
			if (response != null && response.getStatus() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(
								((String) response.getEntity()).getBytes())));
				jsonResponseStr = br.readLine();
			}
		} catch (HttpHostConnectException ee) {
			ee.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonResponseStr;
	}

	public List<MyDeployment> getDeployments() throws IllegalArgumentException {
		String restDeploymentUrl = MscoServerUtils
				.getRestUrlStr(MscoDefines.DEPLOYMENT_REST_URL);
		String jsonResponseStr = getJsonResponseStr(restDeploymentUrl);
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
		activeProcessInstanceList.clear();
		completedProcessInstanceList.clear();
		synchronized (activeProcessInstanceList) {
			System.err.println("RestEngineServiceImpl.deploymentList.size: " + deploymentList.size());
			for (MyDeployment deployment : deploymentList) {
				String restProcessInstanceUrl = MscoDefines
						.getProcessInstanceRestUrl(deployment.getIdentifier());
				
				String jsonResponseStr = getJsonResponseStr(restProcessInstanceUrl);
				jsonResponseStr = jsonResponseStr.substring(18, jsonResponseStr.length() - 1);
				JSONArray processInstanceArray = (JSONArray) JSONSerializer
						.toJSON(jsonResponseStr);

				ProcessInstance processInstance = null;
				try {
					for (Object js : processInstanceArray) {
						JSONObject json = (JSONObject) js;
						processInstance = new ProcessInstance();
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
	public List<ProcessInstance> getProcessInstances(Integer status)
			throws IllegalArgumentException {
		System.out.println("RestEngine.getProcessInstance.status: " + status);
		buildProcessInstanceLists();
		if (status == MscoDefines.ACTIVE_INSTANCE) {
			return activeProcessInstanceList;
		}
		return completedProcessInstanceList;
	}
}
