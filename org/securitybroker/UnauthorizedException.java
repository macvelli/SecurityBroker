package org.securitybroker;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

public class UnauthorizedException extends FilterInterruptException {

	public UnauthorizedException(final HttpServletResponse response, final User user) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User " + user.getName() + " is not authorized to access this resource");
	}

}	// End UnauthorizedException
