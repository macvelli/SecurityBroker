package org.securitybroker.taglib;

import java.util.regex.Pattern;

import javax.servlet.jsp.tagext.TagSupport;

abstract class CommonTag extends TagSupport {

	private static final Pattern spaces = Pattern.compile("[ ]+");

	protected String[]	roles;

	public void setRoles(final String roles) { this.roles = spaces.split(roles.trim()); }

}	// End CommonTag
