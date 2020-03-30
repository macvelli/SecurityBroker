package org.securitybroker.authentication.module;

import javax.servlet.http.HttpSession;

import root.adt.FixedMap;
import root.util.Java;

public class DigestContext {

	private static final String NONCE = "DigestContext.Nonce";
	private static final String OPAQUE = "DigestContext.Opaque";

	public static void saveChallenge(final HttpSession session, final String nOnce, final String opaque) {
		session.setAttribute(NONCE, nOnce);
		session.setAttribute(OPAQUE, opaque);
	}

	public static boolean validRequest(final HttpSession session, final FixedMap<String, String> tokens, final String realm) {
		final String nOnce = (String) session.getAttribute(NONCE);
		final String opaque = (String) session.getAttribute(OPAQUE);

		session.removeAttribute(NONCE);
		session.removeAttribute(OPAQUE);
		return Java.equals(tokens.get("nonce"), nOnce)
			&& Java.equals(tokens.get("opaque"), opaque)
			&& Java.equals(tokens.get("realm"), realm)
			&& "auth".equals(tokens.get("qop"));
	}

}	// End DigestContext
