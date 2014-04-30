package com.msco.mil.client.tan.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.msco.mil.shared.Actor;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyProcessInstance;
import com.msco.mil.shared.Task;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface RestEngineService extends RemoteService {
    public List<MyDeployment> getDeployments() throws IllegalArgumentException;
    public List<MyProcessInstance> getProcessInstances(Integer status) throws IllegalArgumentException;
}
