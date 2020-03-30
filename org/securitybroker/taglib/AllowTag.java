package org.securitybroker.taglib;

import javax.servlet.jsp.JspException;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;

public class AllowTag extends CommonTag {

	private boolean all;

	@Override
	public int doStartTag() throws JspException {
		return isAllowed() ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

	public void setAll(final boolean all) { this.all = all; }

//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Private Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private boolean isAllowed() {
		final User user = SecurityContext.getUser();

		for (String s : roles)
			if (all ^ user.hasCredentials(s))
				return !all;

		return all;
	}

}	// End AllowTag
