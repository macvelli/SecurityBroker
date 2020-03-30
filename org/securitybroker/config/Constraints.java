package org.securitybroker.config;

import org.securitybroker.User;

import root.lang.Array;

public class Constraints {

	private final Array<Constraint>	constraints;

	public Constraints() {
		this.constraints = new Array<Constraint>();
	}

	public void add(final Constraint constraint) {
		constraints.add(constraint);
	}

	public boolean isAuthorized(final String requestURI, final String method, final User user) {
		Constraint constraint = null;

		// TODO: Do we need to find all constraints that match the request URL?  If so, how do we determine a match?
		for (Constraint c : constraints.values())
			if (c.appliesTo(requestURI, method)) {
				constraint = c;
				break;
			}

		if (constraint == null || constraint.roles == null)
			return true;
		else
			for (String s : constraint.roles)
				if (user.hasCredentials(s))
					return true;

		return false;
	}

}	// End Constraints
