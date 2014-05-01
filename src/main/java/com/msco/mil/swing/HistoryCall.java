package com.msco.mil.swing;

import com.msco.mil.server.util.MscoServerUtils;
import com.msco.mil.shared.util.MscoDefines;

public class HistoryCall {
	private static String deploymentId = "org.jbpm:Evaluation:1.0";
	private static String processInstanceId = "1";
	private static String nodeId = "1";
	private static String varId = "1";

	// Get a list of ProcessInstanceLog instances
	private static String processInstanceListStr = "/runtime/" + deploymentId
			+ "/history/instances";

	// Gets the ProcessInstangeLog instance associated with the specified
	// process instance
	private static String processInstanceStr = "/runtime/" + deploymentId
			+ "/history/instance/" + processInstanceId;

	// Gets a list of NodeInstanceLog instances associated with the specified
	// process instance
	private static String nodeInstanceStr = "/runtime/" + deploymentId
			+ "/history/instance/" + processInstanceId + "/node";

	// Gets a list of VariableInstanceLog instances associated with the
	// specified process instance
	private static String variableStr = "/runtime/" + deploymentId
			+ "/history/instance/" + processInstanceId + "/variable";

	// Gets a list of NodeInstanceLog instances associated with the specified
	// process instance that have the given (node) id
	private static String nodeInstanceLogStr = "/runtime/" + deploymentId
			+ "/history/instance/" + processInstanceId + "/node/" + nodeId;

	// Gets a list of VariableInstanceLog instances associated with the
	// specified process instance that have the given (variable) id
	private static String variableInstanceLogStr = "/runtime/" + deploymentId
			+ "/history/instance/" + processInstanceId + "/variable/" + varId;

	public static void main(String[] args) {
		String jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR
						+ processInstanceListStr);
		System.err.println("HistoryCall.Process Instance List: "
				+ jsonResponseStr);

		jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR + processInstanceStr);
		System.err.println("HistoryCall.Process Instance: " + jsonResponseStr);

		jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR + nodeInstanceStr);
		System.err.println("HistoryCall.Node Instance: " + jsonResponseStr);

		jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR + variableStr);
		System.err.println("HistoryCall.Variable: " + jsonResponseStr);

		jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR + nodeInstanceLogStr);
		System.err.println("HistoryCall.Node Instance Log: " + jsonResponseStr);
		
		jsonResponseStr = MscoServerUtils
				.getJsonResponseStr(MscoDefines.REST_STR + variableInstanceLogStr);
		System.err.println("HistoryCall.Variable Instance Log: " + jsonResponseStr);

	}

}
