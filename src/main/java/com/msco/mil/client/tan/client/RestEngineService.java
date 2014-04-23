package com.msco.mil.client.tan.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.msco.mil.shared.Actor;
import com.msco.mil.shared.Deployment;
import com.msco.mil.shared.ProcessInstance;
import com.msco.mil.shared.Task;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface RestEngineService extends RemoteService {
    public List<Deployment> getDeployments() throws IllegalArgumentException;
}
