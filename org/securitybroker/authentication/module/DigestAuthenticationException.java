package org.securitybroker.authentication.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.util.MD5;

import root.servlet.FilterInterruptException;

class DigestAuthenticationException extends FilterInterruptException {

	DigestAuthenticationException(final HttpServletRequest request, final HttpServletResponse response, final String realm, final String key) {
		final String nOnce = generateNOnce(request, key);
		final String opaque = MD5.asHex(nOnce);
		final StringBuilder header = new StringBuilder("Digest realm=\"");

		header.append(realm).append("\", qop=\"auth\", nonce=\"");
		header.append(nOnce).append("\", opaque=\"");
		header.append(opaque).append('"');

		System.out.println("Digest Outgoing Header: " + header.toString());

		DigestContext.saveChallenge(request.getSession(), nOnce, opaque);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", header.toString());
	}

	private String generateNOnce(final HttpServletRequest request, final String key) {
		final StringBuilder builder = new StringBuilder(request.getRemoteAddr());

		builder.append(':').append(System.currentTimeMillis()).append(':').append(key);

		return MD5.asHex(builder.toString());
	}

}	// End DigestAuthenticationException
