package org.securitybroker.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.RedirectURLException;
import org.securitybroker.config.Resource;

import root.lang.SortedArray;

public class SSL extends Resource implements Provider {

	private final int sslPort;

	public SSL(final String url) {
		this(url, 443, null);
	}

	public SSL(final String url, final int sslPort) {
		this(url, sslPort, null);
	}

	public SSL(final String url, final int sslPort, final SortedArray<String> httpMethods) {
		super(url, httpMethods);
		this.sslPort = sslPort;
	}

	public void perform(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		if (request.getScheme().equalsIgnoreCase("http"))
			throw new RedirectURLException(request, response, "https", sslPort);
	}

}	// End SSL
