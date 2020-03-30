package org.securitybroker.config;

class PathPattern extends URLPattern {

	PathPattern(final String pattern) {
		super(pattern.substring(0, pattern.length() - 1));
	}

	public boolean matches(final String url) {
		return (pattern.length() == 1)
			|| ((pattern.length() <= url.length()) && url.startsWith(pattern));
	}

}	// End PathPattern
