package org.securitybroker.authentication.module;

import org.securitybroker.authentication.Manager;

public interface DigestManager extends Manager {

	public String getPassword(String username);

}	// End DigestManager
