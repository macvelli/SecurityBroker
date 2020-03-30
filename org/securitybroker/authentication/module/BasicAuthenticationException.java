package org.securitybroker.authentication.module;

import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

class BasicAuthenticationException extends FilterInterruptException {

	BasicAuthenticationException(final HttpServletResponse response, final String realm) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", new StringBuilder("BASIC realm=\"").append(realm).append('"').toString());
	}

}	// End BasicAuthenticationException
