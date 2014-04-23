package com.msco.mil.shared;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.kie.services.client.api.RestRequestHelper;

import com.sencha.gxt.core.client.util.DateWrapper;

public class MscoUtils {
	public static URL getConsoleUrl() {
		try {
			URL url = new URL("http://localhost:8080/jbpm-console/");
			return url;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static ClientResponse<String> getGetClientResponseInJason(
			String urlString) {
		ClientRequest clientRequest = getClientRequestFactory().createRequest(
				urlString);
		clientRequest.accept("application/json");

		try {
			return clientRequest.get(String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	public static ClientRequestFactory getClientRequestFactory() {
		return RestRequestHelper.createRequestFactory(MscoUtils
				.getConsoleUrl(), MscoDefines.getAuthorization()
				.getUserId(), MscoDefines.getAuthorization()
				.getPassword());
	}

	public static String getRestUrlStr(String componentRestUrl) {
		try {
			URL url = new URL(MscoDefines.SERVER + MscoDefines.JBPM_CONSOLE);
			return new URL(url, url.getPath() + componentRestUrl).toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
