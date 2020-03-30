package org.securitybroker.log;

import java.util.logging.Handler;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;

import root.log.Log;

public class SecurityLog extends Log {

	public SecurityLog(final Class<?> clazz) {
		super(clazz.getName(), dh);
	}

	public SecurityLog(final String name) {
		super(name, dh);
	}

	protected SecurityLog(final String name, final Handler h) {
		super(name, h);
	}

	@Override
	protected String buildMsg(String msg, final Object[] params) {
		final User user = SecurityContext.getUser();

		if (user != null) {
			final StringBuilder builder = new StringBuilder(user.getName().length() + msg.length() + 2);
			msg = builder.append(user.getName()).append(':').append(' ').append(msg).toString();
		}

		return super.buildMsg(msg, params);
	}

}	// End SecurityLog
