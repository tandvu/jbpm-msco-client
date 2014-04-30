package com.msco.mil.server.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.conn.HttpHostConnectException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.kie.services.client.api.RestRequestHelper;

import com.msco.mil.shared.util.MscoDefines;

public class MscoServerUtils {

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
		return RestRequestHelper.createRequestFactory(getConsoleUrl(), MscoDefines.getAuthorization()
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
	
	public static String getJsonResponseStr(String restUrl) {
		String jsonResponseStr = "";

		try {
			// create client response in json
			ClientResponse<String> response = MscoServerUtils
					.getGetClientResponseInJason(restUrl);
System.err.println("MscoServer.getJsonResponseStr.restUrl: " + restUrl + " response: " + response + " status: " + response.getStatus());
			// build deployment list from json response
			if (response != null && response.getStatus() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(
								((String) response.getEntity()).getBytes())));
				jsonResponseStr = br.readLine();
				System.err.println("MscoServerutils.jsonResponsteStr: " + jsonResponseStr);
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

		return jsonResponseStr;
	}
}
