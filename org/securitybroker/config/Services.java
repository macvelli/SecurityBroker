package org.securitybroker.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.service.Provider;

import root.lang.Array;

public class Services {

	private final Array<Provider> providers;

	public Services() {
		this.providers = new Array<Provider>();
	}

	public void add(final Provider provider) {
		providers.add(provider);
	}

	public void perform(final String requestURL, final String method, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		for (Provider p : providers.values())
			if (p.appliesTo(requestURL, method))
				p.perform(request, response);
	}

}	// End Services
