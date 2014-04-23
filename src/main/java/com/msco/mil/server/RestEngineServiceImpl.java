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
import com.msco.mil.shared.Deployment;


@SuppressWarnings("serial")
public class RestEngineServiceImpl extends RemoteServiceServlet implements RestEngineService {
	private JSONArray currentDeploymentArray = new JSONArray();
	private List<Deployment> deploymentList = new ArrayList<Deployment>();
	
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

    public List<Deployment> getDeployments() throws IllegalArgumentException {
		try {			
			//build deployment string
			String deploymentUrlString = new URL(
					MscoClientDefines.getConsoleUrl(),
					MscoClientDefines.getDeploymentUrl()).toExternalForm();

			//create client response in json
			ClientResponse<String> response = MscoClientDefines
					.getGetClientResponseInJason(deploymentUrlString);

			//build deployment list from json response
			if (response != null && response.getStatus() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(
								((String) response.getEntity()).getBytes())));
				String deploymentArray = br.readLine();
				buildDeploymentListFromResponse(deploymentArray);
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
					Deployment deployment = new Deployment();
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
	
	/*
	//==========================================
	
	//common variables defined
	private String deploymentId = "org.jbpm:Evaluation:1.0";
	private String url = "http://localhost:8080/jbpm-console/";
	private String user = "krisv";
	private String password = "krisv";
	private String process = "evaluation";
	private DefaultHttpClient httpclient;
	private UsernamePasswordCredentials credentials;
	private BasicScheme scheme;
	private String restCall;
	private HttpPost httppost;
	private 
	
	 
	//Initializing the HttpClient
	private void initializeHttpClient() {
	  try {
	       httpclient = new DefaultHttpClient();
	       credentials = new UsernamePasswordCredentials(user, password);
	       scheme = new BasicScheme();
	  } catch (Exception ex) {
	       ex.printStackTrace();
	  }
	  }
	 
	//Starting the process
	private void startProcessRestMethod() {
	       restCall = "http://" + "localhost" + ":" + "8080" + "/"
	            + "jbpm-console" + "/rest/runtime/" + deploymentId
	            + "/process/" + process + "/start";
	 
	       try {
	            httppost = new HttpPost(restCall);
	            Header authorizationHeader = scheme.authenticate(credentials, httppost);
	            httppost.addHeader(authorizationHeader);
	            HttpResponse response = httpclient.execute(httppost);
	 
	            if (response != null) {
				log.info("Response status line: " + response.getStatusLine());
	            if (response.getStatusLine() != null) {
	                 log.info("Response status code: "
	                      + response.getStatusLine().getStatusCode());
	            }
	 
	            HttpEntity entity = response.getEntity();
	 
	            if (entity != null) {
	                 String output = EntityUtils.toString(entity);
	                 createProcessInstanceResponse(output);
	            }
	       }
	      } catch (ClientProtocolException ex) {
	            ex.printStackTrace();
	       } catch (AuthenticationException ex) {
	            ex.printStackTrace();
	       } catch (Exception ex) {
	            ex.printStackTrace();
	       } finally {
	            closeHttpClient();
	       }
	  }
	 
	//Getting the process start response
	private void createProcessInstanceResponse(String output) {
	       try {
	            JAXBContext jaxbContext = JAXBContext.newInstance(JaxbProcessInstanceResponse.class);
	            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	            processResponse = (JaxbProcessInstanceResponse) unmarshaller.unmarshal(new StringReader(output));
	            log.info("processResponse: " + processResponse);
	            log.info("id: " + processResponse.getId());
	            log.info("process id: " + processResponse.getProcessId());
	            log.info("process state: " + processResponse.getState());
	       } catch (Exception e) {
	            e.printStackTrace();
	       }
	  }
	 
	//Getting tasks for a user
	private void getTasksForUser() {
	       // get list of tasks
	       restCall = "http://" + "localhost" + ":" + "8080" + "/"
	            + "jbpm-console" + "/rest/task/query?taskOwner=" + user;
	 
	       try {
	 
	            httpget = new HttpGet(restCall);
	            authorizationHeader = scheme.authenticate(credentials, httpget);
	            httpget.addHeader(authorizationHeader);
	            response = httpclient.execute(httpget);
	 
	            if (response != null) {
	                 log.info("Task Response status line: "+ response.getStatusLine());
	                 if (response.getStatusLine() != null) {
	                      log.info("Task Response status code: "+ response.getStatusLine().getStatusCode());
	                 }
	 
	            entity = response.getEntity();
	            String output = EntityUtils.toString(entity);
	 
	            if (entity != null) {
	                 createTaskSummaryResponse(output);
	            }
	            }
	       } catch (ClientProtocolException ex) {
	            ex.printStackTrace();
	       } catch (AuthenticationException ex) {
	            ex.printStackTrace();
	       } catch (Exception ex) {
	            ex.printStackTrace();
	       } finally {
	            closeHttpClient();
	       }
	}
	 
	//Process the response for the task query
	private void createTaskSummaryResponse(String output) {
	  try {
	       JAXBContext jaxbContext = JAXBContext.newInstance(JaxbTaskSummaryListResponse.class);
	       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	       JaxbTaskSummaryListResponse response = (JaxbTaskSummaryListResponse) unmarshaller.unmarshal(new StringReader(output));
	       taskSummary = response.getResult();
	       log.info("taskSummary:"+ taskSummary+ ", length:"+ (taskSummary != null ? taskSummary.size(): "taskSummary is null"));
	  } catch (Exception ex) {
	       ex.printStackTrace();
	  }
	  }
	 
	//Start a task
	private void startTask(String taskId) {
	       restCall = "http://" + "localhost" + ":" + "8080" + "/"
	            + "jbpm-console" + "/rest/task/" + taskId + "/start";
	 
	       try {
	            httppost = new HttpPost(restCall);
	            authorizationHeader = scheme.authenticate(credentials, httppost);
	            httppost.addHeader(authorizationHeader);
	            response = httpclient.execute(httppost);
	 
	            if (response != null) {
	                 log.info("Task start Response status line: "+ response.getStatusLine());
	                 if (response.getStatusLine() != null) {
	                      log.info("Task start Response status code: "+ response.getStatusLine().getStatusCode());
	            }
	            }
	       } catch (ClientProtocolException ex) {
	            ex.printStackTrace();
	       } catch (AuthenticationException ex) {
	            ex.printStackTrace();
	       } catch (Exception ex) {
	            ex.printStackTrace();
	       } finally {
	            closeHttpClient();
	       }
	}

	//Complete a task
	private void completeTask(String taskId) {
	       restCall = "http://" + "localhost" + ":" + "8080" + "/"
	            + "jbpm-console" + "/rest/task/" + taskId + "/complete";
	 
	  try {
	  httppost = new HttpPost(restCall);
	  authorizationHeader = scheme.authenticate(credentials, httppost);
	  httppost.addHeader(authorizationHeader);
	  response = httpclient.execute(httppost);
	 
	  if (response != null) {
	       log.info("Task complete Response status line: "+ response.getStatusLine());
	       if (response.getStatusLine() != null) {
	            log.info("Task complete Response status code: "+ response.getStatusLine().getStatusCode());
	       }
	  }
	  } catch (ClientProtocolException ex) {
	       ex.printStackTrace();
	  } catch (AuthenticationException ex) {
	       ex.printStackTrace();
	  } catch (Exception ex) {
	       ex.printStackTrace();
	  } finally {
	       closeHttpClient();
	  }
	  }
	//==========================================*/
}
