package org.securitybroker.authentication.module;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;
import org.securitybroker.authentication.Manager;
import org.securitybroker.authentication.Module;

import sun.misc.BASE64Decoder;

public class Basic implements Module {

	private final String		realm;
	private final Manager		manager;
	private final BASE64Decoder	decoder;

	public Basic(final Manager app) {
		this("Secured", app);
	}

	public Basic(final String realm, final Manager manager) {
		this.realm = realm;
		this.manager = manager;
		decoder = new BASE64Decoder();
	}

	public boolean isAuthRequired(final HttpServletRequest request) {
		return SecurityContext.getUser(request) == null;
	}

	public User authenticate(final HttpServletRequest request, final HttpServletResponse response) {
		// 1. Initiate the BASIC authentication process, if necessary
		final String auth = request.getHeader("Authorization");
		if (auth == null || !auth.startsWith("Basic "))
			throw new BasicAuthenticationException(response, realm);

		// 2. Create a User object with the user name and password
		String[] split;
		try {
			split = new String(decoder.decodeBuffer(auth.substring(6))).split(":");
		} catch (IOException e) {
			return null;
		}
		User user = new User(split[0], split[1]);

		// 3. Verify the user credentials
		if (!manager.verify(user))
			return null;

		// 4. Load the application-managed User object and save into the context
		user = manager.load(user);
		SecurityContext.setUser(user, request);

		return user;
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) {
		throw new BasicAuthenticationException(response, realm);
	}

}	// End Basic
