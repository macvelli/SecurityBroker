package org.securitybroker.config;

// URL-patterns must be one of the following:
//
//		- start with a '/' and end with a '/*', use this one for path patterns
//		- start with a '*.' and end with a character, use this for extension mapping
//		- some sequence of characters starting with '/' and not ending in '*', use this for exact matches.
//
// This comes from the Servlet spec, so we have to at least support this much.
// But there is nothing stopping us from supporting path extension patterns either.
public abstract class URLPattern {

	protected final String pattern;

	protected URLPattern(final String pattern) {
		this.pattern = pattern;
	}

	public abstract boolean matches(String url);

}	// End URLPattern
