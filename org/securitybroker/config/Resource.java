package org.securitybroker.config;

import root.lang.SortedArray;

public class Resource extends URLResource  {

	private final SortedArray<String> httpMethods;

	public Resource(final String url) {
		this(url, null);
	}

	public Resource(final String url, final SortedArray<String> httpMethods) {
		super(url);
		this.httpMethods = httpMethods;
	}

	public boolean appliesTo(final String url, final String method) {
		return (httpMethods == null || httpMethods.contains(method))
				? pattern.matches(url)
				: false;
	}

}	// End Resource
