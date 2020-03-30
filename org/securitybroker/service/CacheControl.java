package org.securitybroker.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.config.URLResource;

public class CacheControl extends URLResource implements Provider {

	private final boolean noCache;

	public CacheControl(final String url) {
		this(url, true);
	}

	public CacheControl(final String url, final boolean noCache) {
		super(url);
		this.noCache = noCache;
	}

	public boolean appliesTo(final String url, final String method) {
		return ((!"POST".equalsIgnoreCase(method)))
				? pattern.matches(url)
				: false;
	}

	public void perform(final HttpServletRequest request, final HttpServletResponse response) {
		if (noCache) {
			response.setHeader("Cache-Control","no-cache");
			response.addHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
		} else
			response.setHeader("Cache-Control", "private");
	}

}	// End CacheControl
