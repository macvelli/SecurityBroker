package org.securitybroker;

import root.lang.SortedArray;

public class User implements java.io.Serializable {

	protected final String			name;
	protected final String			pass;
	protected SortedArray<String>	credentials;

	public User(final String name, final String password) {
		this.name = (name != null) ? name : "";
		this.pass = (password != null) ? password : "";
	}

	public User(final User user, final String... credentials) {
		this.name = user.name;
		this.pass = user.pass;
		if (credentials.length > 0)
			this.credentials = new SortedArray<String>(credentials);
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return pass;
	}

	public boolean hasCredentials(final String role) {
		return (credentials == null) ? false : credentials.contains(role);
	}

}	// End User
