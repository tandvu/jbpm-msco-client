package com.msco.mil.swing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.drools.core.command.runtime.process.GetProcessIdsCommand;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

public class SimpleRestClient
{
    public static void main( String[] args ) throws MalformedURLException
    {
        String deploymentId = "org.jbpm:Evaluation:1.0";
        URL appUrl = new URL("http://localhost:8080/jbpm-console/");
        String user = "krisv";
        String password = "krisv";
        RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(deploymentId, appUrl, user, password);
       
        RemoteRuntimeEngine engine = restSessionFactory.newRuntimeEngine();    
        KieSession ksession = engine.getKieSession();
        GetProcessIdsCommand getProcessIdsCommand = new GetProcessIdsCommand();
        

       
        Collection<ProcessInstance> processInstanceList = ksession.getProcessInstances();
        System.err.println("SimpleRestClient.processInstanceLise.size: " + processInstanceList.size());
        for (ProcessInstance processInstance : processInstanceList)
        {
        	System.err.println("SimpleRestclientProcessInstance.id: " + processInstance.getId());
        }
        //ksession.execute(getProcessInstancesCommand);
//        List<String> processIds =  ksession.execute(getProcessIdsCommand);
//        System.out.println("The list of process ids"+processIds);
//        for(String pid : processIds){
//            System.out.println("Process Id: "+pid);
//        }
    }  
}
