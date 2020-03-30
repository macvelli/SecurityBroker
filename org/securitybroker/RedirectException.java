package org.securitybroker;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

public class RedirectException extends FilterInterruptException {

	public RedirectException(final String uri, final HttpServletResponse response) throws IOException {
		response.sendRedirect(response.encodeRedirectURL(uri));
	}

}	// End RedirectException
