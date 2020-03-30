package org.securitybroker.authentication.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;
import org.securitybroker.authentication.Module;
import org.securitybroker.util.MD5;

import root.adt.FixedMap;

public class Digest implements Module {

	final String				realm;

	private final String		key;
	private final DigestManager	manager;

	public Digest(final String key, final DigestManager manager) {
		this("Secured", key, manager);
	}

	public Digest(final String realm, final String key, final DigestManager manager) {
		this.realm = realm;
		this.key = key;
		this.manager = manager;
	}

	public boolean isAuthRequired(final HttpServletRequest request) {
		return SecurityContext.getUser(request) == null;
	}

	public User authenticate(final HttpServletRequest request, final HttpServletResponse response) {
		// 1. Initiate the DIGEST authentication process, if necessary
		final String auth = request.getHeader("Authorization");
		if (auth == null || !auth.startsWith("Digest "))
			throw new DigestAuthenticationException(request, response, realm, key);

		// 2. Create a User object with the user name and password
		User user = getUser(request, auth);

		// 3. Verify the user credentials
		if (user == null || !manager.verify(user))
			return null;

		// 4. Load the application-managed User object and save into the context
		user = manager.load(user);
		SecurityContext.setUser(user, request);

		return user;
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		throw new DigestAuthenticationException(request, response, realm, key);
	}

//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Private Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private User getUser(final HttpServletRequest request, final String digestHeader) {
		final String[] nvPairs = digestHeader.substring(7, digestHeader.lastIndexOf('"')).split("(=\"?|\"?, )");
		final FixedMap<String, String> tokens = new FixedMap<String, String>(nvPairs.length >>> 1);

		for (int i=0; i < nvPairs.length; )
			tokens.put(nvPairs[i++], nvPairs[i++]);

		if (!DigestContext.validRequest(request.getSession(), tokens, realm))
			return null;

		final String username = tokens.get("username");
		final String password = manager.getPassword(username);
		final String serverDigest = generateDigest(username, password, request.getMethod(), tokens);

		return (serverDigest.equals(tokens.get("response")))
				? new User(username, password)
				: null;
	}

	private String generateDigest(final String username, final String password, final String method, final FixedMap<String, String> tokens) {
		StringBuilder builder = new StringBuilder(username);

		builder.append(':').append(tokens.get("realm"));
		builder.append(':').append(password);

		final String hashOne = MD5.asHex(builder.toString());

		builder = new StringBuilder(method);
		builder.append(':').append(tokens.get("uri"));

		final String hashTwo = MD5.asHex(builder.toString());

		builder = new StringBuilder(hashOne);
		builder.append(':').append(tokens.get("nonce"));
		builder.append(':').append(tokens.get("nc"));
		builder.append(':').append(tokens.get("cnonce"));
		builder.append(':').append(tokens.get("qop"));
		builder.append(':').append(hashTwo);

		return MD5.asHex(builder.toString());
	}

}	// End Digest
