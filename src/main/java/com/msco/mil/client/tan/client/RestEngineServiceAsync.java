package com.msco.mil.client.tan.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.msco.mil.shared.MyDeployment;
import com.msco.mil.shared.MyProcessInstance;


public interface RestEngineServiceAsync {
    /**
     * GWT-RPC service asynchronous (client-side) interface
     * 
     * @see com.msco.mil.client.RestEngineService
     */
    void getDeployments(AsyncCallback<List<MyDeployment>> callback);
    void getProcessInstances(Integer status, AsyncCallback<List<MyProcessInstance>> callback);

    /**
     * GWT-RPC service asynchronous (client-side) interface
     * 
     * @see com.msco.mil.client.GreetingService
     */
//    void undeploy(java.lang.String deploymentIdentifier, AsyncCallback<java.lang.String> callback);
    
    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {
        private static RestEngineServiceAsync instance;
        
        public static final RestEngineServiceAsync getInstance() {
            if (instance == null) {
                instance = (RestEngineServiceAsync) GWT.create(RestEngineServiceAsync.class);
            }
            return instance;
        }
        
        private Util() {
            // Utility class should not be instanciated
        }
    }
}

