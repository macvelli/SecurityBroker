package org.securitybroker.config;

public class URLResource {

	protected final URLPattern pattern;

	public URLResource(final String url) {
		if (url.startsWith("/"))
			pattern = (url.endsWith("/*")) ? new PathPattern(url) : new ExactPattern(url);
		else if (url.startsWith("*."))
			pattern = new ExtensionPattern(url);
		else
			throw new RuntimeException("Invalid URL Pattern: " + url);
	}

}	// End URLResource
