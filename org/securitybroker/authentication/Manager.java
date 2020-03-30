package org.securitybroker.authentication;

import org.securitybroker.User;

public interface Manager {

	public User load(User user);

	public boolean verify(User user);

}	// End Manager
