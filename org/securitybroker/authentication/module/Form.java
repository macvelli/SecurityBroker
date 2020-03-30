package org.securitybroker.authentication.module;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.SecurityContext;
import org.securitybroker.ForwardException;
import org.securitybroker.RedirectException;
import org.securitybroker.User;
import org.securitybroker.authentication.Manager;
import org.securitybroker.authentication.Module;

public class Form implements Module {

	private final String	loginPage;
	private final String	loginErrorPage;
	private final Manager	manager;

	public Form(final String loginPage, final Manager app) {
		this(loginPage, loginPage, app);
	}

	public Form(final String loginPage, final String loginErrorPage, final Manager manager) {
		this.loginPage = loginPage;
		this.loginErrorPage = loginErrorPage;
		this.manager = manager;
	}

	public boolean isAuthRequired(final HttpServletRequest request) {
		return SecurityContext.getUser(request) == null;
	}

	public User authenticate(final HttpServletRequest request, final HttpServletResponse response) {
		// 1. Initiate the FORM authentication process, if necessary
		if (!request.getRequestURI().endsWith("/j_security_check")) {
			SecurityContext.setRedirectURL(request);
			try {
				throw new ForwardException(request, response, loginPage);
			} catch (Exception e) {
				return null;
			}
		}

		// 2. Create a User object with the user name and password
		User user = new User(request.getParameter("j_username"), request.getParameter("j_password"));

		// 3. Verify the user credentials
		if (!manager.verify(user))
			return null;

		// 4. Load the application-managed User object and save into the context
		user = manager.load(user);
		SecurityContext.setUser(user, request);

		// 5. Redirect to the protected URL
		try {
			throw new RedirectException(SecurityContext.getRedirectURL(request), response);
		} catch (IOException e) {
			return null;
		}
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		throw new ForwardException(request, response, loginErrorPage);
	}

}	// End Form
