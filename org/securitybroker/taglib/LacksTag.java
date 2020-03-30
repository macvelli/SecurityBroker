package org.securitybroker.taglib;

import javax.servlet.jsp.JspException;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;

public class LacksTag extends CommonTag {

	@Override
	public int doStartTag() throws JspException {
		return isLacking() ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Private Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private boolean isLacking() {
		final User user = SecurityContext.getUser();

		for (String s : roles)
			if (user.hasCredentials(s))
				return false;

		return true;
	}

}	// End LacksTag
