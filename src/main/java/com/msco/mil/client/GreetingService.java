package com.msco.mil.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.msco.mil.shared.Actor;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.ProcessInstance;
import com.msco.mil.shared.Task;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    public List<MyDeployment> getDeployments() throws IllegalArgumentException;
    public List<ProcessInstance> getProcessInstances(Integer status) throws IllegalArgumentException;
    public List<Task> getTasks() throws IllegalArgumentException;
    public List<Actor> getActors() throws IllegalArgumentException;
    public String undeploy(String deploymentIdentifier) throws IllegalArgumentException;
}
