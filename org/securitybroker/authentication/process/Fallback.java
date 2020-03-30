package org.securitybroker.authentication.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;
import org.securitybroker.authentication.Module;
import org.securitybroker.authentication.Process;

public class Fallback implements Process {

	private static final String BACKUP_MODULE = "Backup.Module";

	private final Module primary;
	private final Module backup;

	public Fallback(final Module primary, final Module backup) {
		this.primary = primary;
		this.backup = backup;
	}

	public User getUser(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final HttpSession session = request.getSession();
		final boolean usePrimary = (session.getAttribute(BACKUP_MODULE) == null);
		final Module module = (usePrimary) ? primary : backup;
		User user = (module.isAuthRequired(request))
							? module.authenticate(request, response)
							: SecurityContext.getUser(request);

		if (user == null && usePrimary) {
			session.setAttribute(BACKUP_MODULE, new Object());
			user = backup.authenticate(request, response);
		}

		return user;
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		backup.failedLogin(request, response);
	}

}	// End BackupModule
