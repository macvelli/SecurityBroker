package org.securitybroker.config;

class ExtensionPattern extends URLPattern {

	ExtensionPattern(final String pattern) {
		super(pattern.substring(1));
	}

	public boolean matches(final String url) {
		return url.endsWith(pattern);
	}

}	// End ExtensionPattern
