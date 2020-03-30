package org.securitybroker.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Provider {

	public boolean appliesTo(String requestURL, String method);

	public void perform(HttpServletRequest request, HttpServletResponse response) throws Exception;

}	// End Provider
