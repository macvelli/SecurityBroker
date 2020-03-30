package org.securitybroker;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

public class RedirectURLException extends FilterInterruptException {

	public RedirectURLException(final HttpServletRequest request, final HttpServletResponse response, final String scheme, final int port) throws IOException {
		final String pathInfo = request.getPathInfo();
		final String queryString = request.getQueryString();
		final StringBuilder redirectUrl = new StringBuilder(scheme);

		redirectUrl.append("://");
		redirectUrl.append(request.getServerName());
		if (port != 80 && port != 443)
			redirectUrl.append(':').append(port);
		redirectUrl.append(request.getContextPath()).append(request.getServletPath());
		if (pathInfo != null)
			redirectUrl.append(pathInfo);
		if (queryString != null)
			redirectUrl.append('?').append(queryString);

		response.sendRedirect(response.encodeRedirectURL(redirectUrl.toString()));
	}

}	// End RedirectURLException
