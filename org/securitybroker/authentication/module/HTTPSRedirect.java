package org.securitybroker.authentication.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.RedirectURLException;
import org.securitybroker.User;
import org.securitybroker.authentication.Module;

// TODO: Need to test this with a live SSL connection and various authentication schemes (i.e. Basic, Form, etc)
public class HTTPSRedirect implements Module {

	private int				httpPort;
	private final int		sslPort;
	private final Module	module;

	public HTTPSRedirect(final Module module, final int sslPort) {
		httpPort = -1;
		this.sslPort = sslPort;
		this.module = module;
	}

	public boolean isAuthRequired(final HttpServletRequest request) {
		return module.isAuthRequired(request);
	}

	public User authenticate(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		if (request.getScheme().equalsIgnoreCase("http")) {
			if (httpPort < 0)
				httpPort = request.getServerPort();

			throw new RedirectURLException(request, response, "https", sslPort);
		}

		module.authenticate(request, response);

		throw new RedirectURLException(request, response, "http", httpPort);
	}

	public void failedLogin(final HttpServletRequest request,	final HttpServletResponse response) throws Exception {
		module.failedLogin(request, response);
	}

}	// End HTTPSRedirect
