package com.msco.mil.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.process.Process;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.task.model.TaskSummary;
import org.kie.api.task.model.User;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.RestRequestHelper;
import org.kie.services.client.serialization.jaxb.impl.deploy.JaxbDeploymentJobResult;
import org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;

import com.msco.mil.client.GreetingService;
import com.msco.mil.shared.Actor;
import com.msco.mil.shared.Deployment;
import com.msco.mil.shared.ProcessInstance;
import com.msco.mil.shared.Task;
import com.google.gwt.thirdparty.streamhtmlparser.util.EntityResolver.Status;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
    private List<Deployment> deploymentList = new ArrayList<Deployment>();
    private List<ProcessInstance> activeProcessInstanceList = new ArrayList<ProcessInstance>();
    private List<ProcessInstance> completedProcessInstanceList = new ArrayList<ProcessInstance>();
    private List<Task> taskList = new ArrayList<Task>();
    private List<Actor> actorList = new ArrayList<Actor>();
    JSONArray currentDeploymentArray = new JSONArray();
    int currentDeploymentIncrement = 0;
    
    // private static final Logger logger =
    // LoggerFactory.getLogger(GreetingServiceImpl.class);
    public GreetingServiceImpl() {
        System.out.println("hello");
        Actor actor = new Actor();
        actor.setName("krisv");
        actor.setColor("black");
        actorList.add(actor);
        actor = new Actor();
        actor.setName("admin");
        actor.setColor("black");
        actorList.add(actor);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        // System.out.println("currentDeploymentIncrement:" +
                        // currentDeploymentIncrement);
                        processDeploymentList();
                        processProcessInstanceList();
                        processTaskList();
                        
                        
                        Thread.sleep(5000);
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
    
    public void getProcessInstances(String deploymentId) {
        URL appUrl = null;
        try {
            appUrl = new URL("http://localhost:8080/jbpm-console/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String user = "admin";
        String password = "admin";
        RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(deploymentId, appUrl, user, password);
        RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
        KieSession ksession = engine.getKieSession();
        List<TaskSummary> tasks = engine.getTaskService().getTasksAssignedAsPotentialOwner("krisv", "en-UK");
        System.out.println("kiebase"); // KieBase base =
        ksession.getKieBase();
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.getKieClasspathContainer();
        KieBase b = kContainer.getKieBase();
        System.out.println(deploymentId + " tasks size:" + tasks.size());
//        Collection<Process> processList = b.getProcesses();
//        for (Process process : processList)
//        {
//        	System.err.println("GreetingServiceImpl.process.id: " + process.getId());
//        }
    }
    
    public void parseDeploymentListJson(String the_json) {
        try {
            JSONArray deploymentArray = (JSONArray) JSONSerializer.toJSON(the_json);
            if (currentDeploymentArray.equals(deploymentArray)) {
                return;
            }
            currentDeploymentArray = deploymentArray;
            currentDeploymentIncrement++;
            // clear because there might be a previous data
            synchronized (deploymentList) {
                deploymentList.clear();
                for (Object js : currentDeploymentArray) {
                    JSONObject json = (JSONObject) js;
                    // System.out.println(json.get("groupId") + " " +
                    // json.get("status") + " " + json.get("version"));
                    // System.out.println(json.toString());
                    Deployment deployment = new Deployment();
                    deployment.setArtifactId((String) json.get("artifactId"));
                    deployment.setGroupId((String) json.get("groupId"));
                    deployment.setKbaseName((String) json.get("kbaseName"));
                    deployment.setKsessionName((String) json.get("ksessionName"));
                    deployment.setStatus((String) json.get("status"));
                    deployment.setStrategy((String) json.get("strategy"));
                    deployment.setVersion((String) json.get("version"));
                    deployment.setIdentifier((String) json.get("identifier"));
//                    System.err.println("GreetingServiceImpl.deployment.getIdentifier(): " + deployment.getIdentifier());
                    deploymentList.add(deployment);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void processTaskList() throws Exception {
        taskList.clear();
        synchronized (taskList) {
            for (ProcessInstance pi : activeProcessInstanceList) {
                String initiator = pi.getInitiator();
                URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
                ClientRequestFactory requestFactory;
                try {
                    requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, initiator, initiator);
                    String urlString = new URL(deploymentUrl, deploymentUrl.getPath()
                            + "rest/task/query?processInstanceId=" + pi.getId()).toExternalForm();
                    // System.out.println(urlString);
                    ClientRequest restRequest = requestFactory.createRequest(urlString);
                    // restRequest.accept("application/json");
                    ClientResponse<?> response = checkResponse(restRequest.get());
                    JaxbTaskSummaryListResponse taskSummaryList = response.getEntity(JaxbTaskSummaryListResponse.class);
                    // System.out.println("status:" + response.getStatus());
                    if (response.getStatus() == 200) {
                        List<TaskSummary> ts = taskSummaryList.getResult();
                        for (TaskSummary s : ts) {
                            Task task = new Task();
                            task.setCreatedOn(s.getCreatedOn());
                            task.setExpiration(s.getExpirationTime());
                            task.setId(s.getId());
                            task.setName(s.getName());
                            task.setPriority(s.getPriority());
                            task.setStatus(s.getStatus().name());
                            task.setAction1("Skip");
                            s.getActualOwner();
                            User user = s.getActualOwner();
                            task.setOwner(user.getId());
                            Long instanceId = s.getProcessInstanceId();
                            for (ProcessInstance p : activeProcessInstanceList) {
                                if (p.getId() == instanceId.intValue()) {
                                    task.setParentName(p.getName());
                                    task.setDeployment(p.getExternalId());
                                }
                            }
                            /*
                             * System.out.println(s.getName());
                             * System.out.println(s.getId());
                             * System.out.println(s.getPriority());
                             * System.out.println(s.getStatus().name());
                             * System.out.println(s.getCreatedOn());
                             * System.out.println(s.getExpirationTime());
                             */
                            taskList.add(task);
                        }
                    }
                }
                catch (HttpHostConnectException ee) {
                }
                catch (SocketTimeoutException e) {
                }
            }
        }
    }
    
    public void processProcessInstanceList() throws Exception {
        URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
        String userId = "krisv";
        String password = "krisv";
        ClientRequestFactory requestFactory;
        activeProcessInstanceList.clear();
        completedProcessInstanceList.clear();
        synchronized (activeProcessInstanceList) {
            for (Deployment d : deploymentList) {
                try {
                    requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, userId, password);
                    String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/runtime/"
                            + d.getIdentifier() + "/history/instances").toExternalForm();
                    // System.out.println(urlString);
                    ClientRequest restRequest = requestFactory.createRequest(urlString);
                    restRequest.accept("application/json");
                    ClientResponse<String> response = restRequest.get(String.class);
                    // System.out.println("status:" + response.getStatus());
                    if (response.getStatus() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                                ((String) response.getEntity()).getBytes())));
                        // System.out.println("br:" + br.readLine());
                        ProcessInstance processInstance = null;
                        String str = br.readLine();
                        str = str.substring(18, str.length() - 1);
                        // System.out.println("new br:" + str);
                        try {
                            JSONArray processInstanceArray = (JSONArray) JSONSerializer.toJSON(str);
                            for (Object js : processInstanceArray) {
                                JSONObject json = (JSONObject) js;
                                System.out.println(json.toString());
                                processInstance = new ProcessInstance();
                                Integer id = (Integer) json.get("id");
                                Long idLong = id.longValue();
                                processInstance.setId(idLong);
                                processInstance.setName((String) json.get("processName"));
                                processInstance.setInitiator((String) json.get("identity"));
                                processInstance.setVersion((String) json.get("processVersion"));
                                processInstance.setStartDate((Long) json.get("start"));
                                processInstance.setExternalId((String) json.get("externalId"));
                                Long date = (Long) json.get("start");
                                Integer state = (Integer) json.get("status");
                                processInstance.setDate(new Date(date));
                                processInstance.setState("Unknown");
                                if (state == 1) {
                                    activeProcessInstanceList.add(processInstance);
                                    processInstance.setState("Active");
                                }
                                else if (state == 2) {
                                    completedProcessInstanceList.add(processInstance);
                                    processInstance.setState("Completed");
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (HttpHostConnectException ee) {
                }
                catch (SocketTimeoutException e) {
                }
            }
        }
    }
    
    public void getProcessInstanceList(String deploymentId) throws Exception {
        URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
        String userId = "krisv";
        String password = "krisv";
        ClientRequestFactory requestFactory;
        try {
            requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, userId, password);
            String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/runtime/" + deploymentId
                    + "/history/instances").toExternalForm();
            // urlString = new URL(deploymentUrl, deploymentUrl.getPath() +
            // "rest/process").toExternalForm();
            System.out.println(urlString);
            ClientRequest restRequest = requestFactory.createRequest(urlString);
            restRequest.accept("application/json");
            ClientResponse<String> response = restRequest.get(String.class);
            System.out.println("status:" + response.getStatus());
            // if (response.getStatus() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                    ((String) response.getEntity()).getBytes())));
            System.out.println("br:" + br.readLine());
            // parseDeploymentListJson(br.readLine());
            // }
        }
        catch (HttpHostConnectException ee) {
        }
        catch (SocketTimeoutException e) {
        }
    }
    
    public void processDeploymentList() throws Exception {
        URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
        String userId = "krisv";
        String password = "krisv";
        ClientRequestFactory requestFactory;
        try {
            requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, userId, password);
            String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/deployment").toExternalForm();
            ClientRequest restRequest = requestFactory.createRequest(urlString);
            restRequest.accept("application/json");
            ClientResponse<String> response = restRequest.get(String.class);
            if (response.getStatus() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                        ((String) response.getEntity()).getBytes())));
                String str = br.readLine();
                System.out.println("raw:" + str);
                parseDeploymentListJson(str);
            }
        }
        catch (HttpHostConnectException ee) {
        }
        catch (SocketTimeoutException e) {
        }
    }
    
    private static ClientResponse<?> checkResponse(ClientResponse<?> responseObj) throws Exception {
        responseObj.resetStream();
        int status = responseObj.getStatus();
        if (status != 200) {
            // assertEquals("Status OK", 200, status);
        }
        return responseObj;
    }
    
    public void unDeploy(String deploymentId) throws Exception {
        URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
        ClientRequestFactory requestFactory;
        String userId = "krisv";
        String password = "krisv";
        requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, userId, password);
        String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/deployment/" + deploymentId
                + "/undeploy").toExternalForm();
        ClientRequest restRequest = requestFactory.createRequest(urlString);
        // Post, get response, check status response, and get info
        ClientResponse<?> responseObj = checkResponse(restRequest.post());
        JaxbDeploymentJobResult processInstance = responseObj.getEntity(JaxbDeploymentJobResult.class);
        System.out.println(responseObj.getStatus() + " " + processInstance.isSuccess() + " "
                + processInstance.getExplanation());
    }
    
    public void test(String deploymentId) throws Exception {
        URL deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
        ClientRequestFactory requestFactory;
        String userId = "krisv";
        String password = "krisv";
        requestFactory = RestRequestHelper.createRequestFactory(deploymentUrl, userId, password);
        String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/deployment/" + deploymentId
                + "/content").toExternalForm();
        ClientRequest restRequest = requestFactory.createRequest(urlString);
        System.out.println(urlString);
        restRequest.accept("application/json");
        ClientResponse<String> response = restRequest.get(String.class);
        System.out.println(response.getStatus());
        if (response.getStatus() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                    ((String) response.getEntity()).getBytes())));
            System.out.println(br.readLine());
        }
    }
    
    public List<Deployment> getDeployments() throws IllegalArgumentException {
        return deploymentList;
    }
    
    public String undeploy(String deploymentId) throws IllegalArgumentException {
        try {
            unDeploy(deploymentId);
            Thread.sleep(1000);
            processDeploymentList();
            for (Deployment d : deploymentList) {
                if (d.getIdentifier().equals(deploymentId)) {
                    return "Deployment may have processes running, cannot undeploy.";
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Successfuly undeployed.";
    }
    
    public List<ProcessInstance> getProcessInstances(Integer request) throws IllegalArgumentException {
        if (request == 1) {
            return activeProcessInstanceList;
        }
        return completedProcessInstanceList;
    }
    
    public List<Task> getTasks() throws IllegalArgumentException {
        return taskList;
    }
    
    public List<Actor> getActors() throws IllegalArgumentException {
        return actorList;
    }
}
