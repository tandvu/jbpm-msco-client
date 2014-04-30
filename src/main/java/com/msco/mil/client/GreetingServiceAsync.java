package com.msco.mil.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.msco.mil.shared.MyDeployment;


public interface GreetingServiceAsync {
    /**
     * GWT-RPC service asynchronous (client-side) interface
     * 
     * @see com.msco.mil.client.GreetingService
     */
    void getDeployments(AsyncCallback<java.util.List<MyDeployment>> callback);
    
    void getProcessInstances(Integer status, AsyncCallback<java.util.List<com.msco.mil.shared.MyProcessInstance>> callback);
    void getTasks(AsyncCallback<java.util.List<com.msco.mil.shared.Task>> callback);
    void getActors(AsyncCallback<java.util.List<com.msco.mil.shared.Actor>> callback);
    /**
     * GWT-RPC service asynchronous (client-side) interface
     * 
     * @see com.msco.mil.client.GreetingService
     */
    void undeploy(java.lang.String deploymentIdentifier, AsyncCallback<java.lang.String> callback);
    
    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {
        private static GreetingServiceAsync instance;
        
        public static final GreetingServiceAsync getInstance() {
            if (instance == null) {
                instance = (GreetingServiceAsync) GWT.create(GreetingService.class);
            }
            return instance;
        }
        
        private Util() {
            // Utility class should not be instanciated
        }
    }
}
