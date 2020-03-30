package org.securitybroker.authentication.module;

import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

class NTLMNegotiationException extends FilterInterruptException {

	NTLMNegotiationException(final HttpServletResponse response) {
		this(response, null);
	}

	NTLMNegotiationException(final HttpServletResponse response, final String typeTwoMsg) {
		final StringBuilder builder = new StringBuilder("NTLM");

		if (typeTwoMsg != null)
			builder.append(' ').append(typeTwoMsg);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", builder.toString());
		response.setHeader("Connection", "Keep-Alive");
	}

}	// End NTLMNegotiationException
