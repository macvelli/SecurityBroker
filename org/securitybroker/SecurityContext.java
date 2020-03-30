package org.securitybroker;

import javax.servlet.http.HttpServletRequest;

public class SecurityContext {

	private static final String USER_ATTR = "user";
	private static final String REDIRECT_URL = "Redirect.URL";

	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static User getUser() {
		return userHolder.get();
	}

	public static User getUser(final HttpServletRequest request) {
		return (User) request.getSession().getAttribute(USER_ATTR);
	}

	static void setUser(final User user) {
		userHolder.set(user);
	}

	public static void setUser(final User user, final HttpServletRequest request) {
		request.getSession().setAttribute(USER_ATTR, user);
	}

	public static String getRedirectURL(final HttpServletRequest request) {
		final String redirectURL = (String) request.getSession().getAttribute(REDIRECT_URL);

		if (redirectURL != null)
			request.getSession().removeAttribute(REDIRECT_URL);

		return redirectURL;
	}

	public static void setRedirectURL(final HttpServletRequest request) {
		final StringBuilder builder = new StringBuilder(request.getRequestURI());
		final String queryString = request.getQueryString();

		if (queryString != null)
			builder.append('?').append(queryString);

		request.getSession().setAttribute(REDIRECT_URL, builder.toString());
	}

}	// End SecurityContext
