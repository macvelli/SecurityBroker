package org.securitybroker.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.User;

public interface Module {

	public boolean isAuthRequired(HttpServletRequest request);

	public User authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public void failedLogin(HttpServletRequest request, HttpServletResponse response) throws Exception;

}	// End Module
