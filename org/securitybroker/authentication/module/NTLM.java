package org.securitybroker.authentication.module;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbSession;

import org.securitybroker.SecurityContext;
import org.securitybroker.User;
import org.securitybroker.authentication.Manager;
import org.securitybroker.authentication.Module;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class NTLM implements Module {

	private static final String NTLM_CHALLENGE = "Ntlm.Challenge";
	private static final String NTLM_NEGOTIATE = "Ntlm.Negotiate";

	private boolean loadBalance;

	private final String		dc;
	private final Manager		manager;
	private final BASE64Decoder	decoder;
	private final BASE64Encoder	encoder;

	public NTLM(final String domainController, final Manager manager) {
		dc = domainController;
		this.manager = manager;
		decoder = new BASE64Decoder();
		encoder = new BASE64Encoder();
	}

	public void setLoadBalance(final boolean loadBalance) {
		this.loadBalance = loadBalance;
	}

	public void setNetbiosWINS(final String netbiosWINS) {
		Config.setProperty("jcifs.netbios.wins", netbiosWINS);  
	}

	public boolean isAuthRequired(final HttpServletRequest request) {
		return (SecurityContext.getUser(request) == null || reAuthOnIEPost(request));
	}

	public User authenticate(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		// 1. Initiate the NTLM authentication process, if necessary
		final String auth = request.getHeader("Authorization");
		if (auth == null || !auth.startsWith("NTLM "))
			throw new NTLMNegotiationException(response);

		// 2. Create a User object with the user name and password
		User user = getUser(request.getSession(), response, auth);

		// 3. Perform authentication if user not already authenticated
		if (SecurityContext.getUser(request) == null) {
			// a. Verify the user credentials
			if (!manager.verify(user))
				return null;

			// b. Load the application-managed User object and save into the context
			user = manager.load(user);
			SecurityContext.setUser(user, request);
			return user;
		}

		return SecurityContext.getUser(request);
	}

	public void failedLogin(final HttpServletRequest request, final HttpServletResponse response) {
		// TODO: What to do here?  Could I fall back here to another authentication module?
	}

//	**************************** Private Methods *****************************

	private User getUser(final HttpSession session, final HttpServletResponse response, final String authHeader) throws IOException {
		final Object negotiate = session.getAttribute(NTLM_NEGOTIATE);
		final UniAddress dcAddress = this.getDCAddress(session);

		if (negotiate == null) {
			session.setAttribute(NTLM_NEGOTIATE, new Object());
			final Type1Message typeOneMsg = new Type1Message(decoder.decodeBuffer(authHeader.substring(5)));
			final Type2Message typeTwoMsg = new Type2Message(typeOneMsg, this.getChallenge(session, dcAddress), null);
			throw new NTLMNegotiationException(response, encoder.encode(typeTwoMsg.toByteArray()));
		}

		final Type3Message typeThreeMsg = new Type3Message(decoder.decodeBuffer(authHeader.substring(5)));
		final byte[] lmResponse = (typeThreeMsg.getLMResponse() != null) ? typeThreeMsg.getLMResponse() : new byte[0];
		final byte[] ntResponse = (typeThreeMsg.getNTResponse() != null) ? typeThreeMsg.getNTResponse() : new byte[0];
		final NtlmPasswordAuthentication ntlmAuth = new NtlmPasswordAuthentication(typeThreeMsg.getDomain(), typeThreeMsg.getUser(), this.getChallenge(session, dcAddress), lmResponse, ntResponse);

		try {
			SmbSession.logon(dcAddress, ntlmAuth);
		} finally {
			session.removeAttribute(NTLM_NEGOTIATE);
			if (loadBalance)
				session.removeAttribute(NTLM_CHALLENGE);
		}

		return new User(ntlmAuth.getUsername(), ntlmAuth.getPassword());
	}

	private boolean reAuthOnIEPost(final HttpServletRequest request) {
		if (request.getMethod().equalsIgnoreCase("POST")) {
			final String userAgent = request.getHeader("User-Agent");
			return userAgent != null && userAgent.contains("MSIE");
		}

		return false;
	}

	private UniAddress getDCAddress(final HttpSession session) throws IOException {
		if (loadBalance) {
			NtlmChallenge chal = (NtlmChallenge) session.getAttribute(NTLM_CHALLENGE);
			if (chal == null) {
				chal = SmbSession.getChallengeForDomain();
				session.setAttribute(NTLM_CHALLENGE, chal);
			}
			return chal.dc;
		}

		return UniAddress.getByName(dc, true);
	}

	private byte[] getChallenge(final HttpSession session, final UniAddress dcAddress) throws IOException {
		return (loadBalance)
				? ((NtlmChallenge) session.getAttribute(NTLM_CHALLENGE)).challenge
				: SmbSession.getChallenge(dcAddress);
	}

}	// End NTLM
