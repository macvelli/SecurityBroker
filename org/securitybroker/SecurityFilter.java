package org.securitybroker;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.securitybroker.authentication.Process;
import org.securitybroker.config.Constraints;
import org.securitybroker.config.Services;
import org.securitybroker.log.SecurityLog;

import root.log.Log;
import root.servlet.HttpFilter;

// TODO: Interesting PHP framework http://www.symfony-project.org/book/1_0/06-Inside-the-Controller-Layer
// TODO: http://securityfilter.sourceforge.net/
// TODO: http://www.ibm.com/developerworks/web/library/wa-appsec/
// TODO: http://www.developer.com/java/ent/article.php/3467801
// TODO: Now have to check out Spring Security 2 (Formerly Acegi) and check out a) their new config specs b) JSR 250 support -- how did they do this?
// TODO: One thing I liked about SS2 defaults include a built-in login error mechanism for form authentication that displayed error messages to the user on failed login attempt
// TODO: I also noticed they had <sec:asdf> custom JSP tags --  now I have to come up with a different default namespace
// TODO: The default configuration seemed to do a lot for you out of the box, but as with all things that can be both good and bad -- good if you only need the default functionality and bad if you need anything else
// TODO: Why the hell does there need to be external config again?  Ben says because there is usually one team doing the development and another for deployment.  If that is the case, then how would I support external config files?  Sounds like I need to get my ass working on XMLBroker
// TODO: The "Bubba" defense does not work for two main reasons a) every person who deliberately causes a security breach doesn't think they will get caught b) if they do get caught, the judicial system automatically labels them innocent until proven guilty and chances are very good they will get off
public abstract class SecurityFilter extends HttpFilter {

	private static final Log log = new SecurityLog(SecurityFilter.class);

	protected String			loginFailedURL;
	protected String			authFailedURL;
	protected Process			process;
	protected final Services	beforeAuth	= new Services();
	protected final Services	afterAuth	= new Services();
	protected final Constraints	constraints	= new Constraints();

	public abstract void init(FilterConfig config) throws ServletException;

//	*************************** Protected Methods ****************************

	@Override protected void execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final String requestURL = request.getRequestURI();
		final String method = request.getMethod();

		// 1. Perform any services configured before authentication
		// TODO: Services to start with c) Max login attempts d) Redirect default servlet URLs e) Forgot username / password f) User already logged in
		beforeAuth.perform(requestURL, method, request, response);

		// 2. Authenticate and populate the user
		final User user = process.getUser(request, response);

		// 3. Handle the failed login attempt if the user is null
		if (user == null) {
			if (loginFailedURL == null)
				process.failedLogin(request, response);
			else
				throw new ForwardException(request, response, loginFailedURL);
		} else {
			// 4. Set the SecurityContext ThreadLocal User holder
			SecurityContext.setUser(user);

			// 5. Perform any services configured after authentication
			afterAuth.perform(requestURL, method, request, response);

			// 6. Does the user have authority to access the protected resource?
			// TODO: Need to be able to support multiple ways of determining authorization (i.e. JSR-250)
			if (!constraints.isAuthorized(requestURL, method, user)) {
				log.warn("AUTHORIZATION FAILED -- attempted to access {P} using HTTP method {P}", requestURL, method);
				if (authFailedURL == null)
					throw new UnauthorizedException(response, user);
				else
					throw new ForwardException(request, response, authFailedURL);
			}
		}
	}

}	// End SecurityFilter
