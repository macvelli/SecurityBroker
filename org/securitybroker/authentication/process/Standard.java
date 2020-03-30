package org.securitybroker.authentication.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;
import org.securitybroker.authentication.Module;
import org.securitybroker.authentication.Process;

public class Standard implements Process {

	private final Module module;

	public Standard(final Module module) {
		this.module = module;
	}

	public User getUser(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		return (module.isAuthRequired(request))
				? module.authenticate(request, response)
				: SecurityContext.getUser(request);
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		module.failedLogin(request, response);
	}

}	// End Standard
