package com.msco.mil.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.conn.HttpHostConnectException;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.msco.mil.client.tan.client.RestEngineService;
import com.msco.mil.client.tan.client.util.MscoClientDefines;
import com.msco.mil.shared.MyDeployment;


@SuppressWarnings("serial")
public class RestEngineServiceImpl extends RemoteServiceServlet implements RestEngineService {
	private JSONArray currentDeploymentArray = new JSONArray();
	private List<MyDeployment> deploymentList = new ArrayList<MyDeployment>();
	
    public RestEngineServiceImpl() {
        System.out.println("Constructor RestEngineServiceImpl====================");

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                    	/*
                        // System.out.println("currentDeploymentIncrement:" +
                        // currentDeploymentIncrement);
                        processDeploymentList();
                        processProcessInstanceList();
                        processTaskList();
                        */
                    	
                        Thread.sleep(MscoClientDefines.REFRESH_RATE);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    private String getJsonResponseStr(String restUrl)
    {
    	String jsonResponseStr = "";
    	
		try {			
			//create client response in json
			ClientResponse<String> response = MscoClientDefines
					.getGetClientResponseInJason(restUrl);

			//build deployment list from json response
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
		String restDeploymentUrl = MscoClientDefines.getDeploymentUrl();
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
}
