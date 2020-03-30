package org.securitybroker.config;

class ExactPattern extends URLPattern {

	ExactPattern(final String pattern) {
		super(pattern);
	}

	public boolean matches(final String url) {
		return pattern.length() == 1 || url.equals(pattern);
	}

}	// End ExactPattern
