package org.securitybroker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import root.servlet.FilterInterruptException;

public class ForwardException extends FilterInterruptException {

	public ForwardException(final HttpServletRequest request, final HttpServletResponse response, final String path) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		request.getRequestDispatcher(path).forward(request, response);
	}

}	// End ForwardException
