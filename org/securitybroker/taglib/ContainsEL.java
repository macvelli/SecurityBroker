package org.securitybroker.taglib;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;

public class ContainsEL {

	public static boolean contains(final String role) {
		final User user = SecurityContext.getUser();

		return user.hasCredentials(role);
	}

}	// End ContainsEL
