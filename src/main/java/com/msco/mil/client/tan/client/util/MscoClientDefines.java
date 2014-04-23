package com.msco.mil.client.tan.client.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.kie.services.client.api.RestRequestHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.msco.mil.client.sencha.gxt.examples.resources.client.model.Stock;
import com.msco.mil.shared.Authorization;
import com.msco.mil.shared.Deployment;
import com.msco.mil.shared.MyDeploymentProperties;
import com.msco.mil.shared.ProcessInstance;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class MscoClientDefines 
{	
	public static int REFRESH_RATE = 5000;
	
	public static Authorization getAuthorization()
	{
		return new Authorization("krisv", "krisv");
	}
	
	public static URL getConsoleUrl()
	{
		try {
			return new URL("http://localhost:8080/jbpm-console/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}

	public static ClientResponse<String> getGetClientResponseInJason(String urlString)
	{
    	ClientRequest clientRequest = getClientRequestFactory().createRequest(urlString);
    	clientRequest.accept("application/json");
    	
		try {
			return clientRequest.get(String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
	public static ClientRequestFactory getClientRequestFactory()
	{
        return RestRequestHelper.createRequestFactory(
        		MscoClientDefines.getConsoleUrl(), 
        		MscoClientDefines.getAuthorization().getUserId(),
        		MscoClientDefines.getAuthorization().getPassword());
	}
	
	public static String getDeploymentUrl()
	{
		if (getConsoleUrl() != null)
		{
			return getConsoleUrl().getPath() + "rest/deployment";	
		}
		
		return "";
	}
}
