package org.securitybroker.config;

import root.lang.SortedArray;

public class Constraint extends Resource {

	final String[] roles;

	public Constraint(final String url, final String... roles) {
		this(url, null, roles);
	}

	public Constraint(final String url, final SortedArray<String> httpMethods, final String... roles) {
		super(url, httpMethods);
		this.roles = (roles.length == 0 || roles.length == 1 && roles[0].equals("*")) ? null : roles;
	}

}	// End Constraint
