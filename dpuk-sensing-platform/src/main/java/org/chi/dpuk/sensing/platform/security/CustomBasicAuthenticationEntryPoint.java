package org.chi.dpuk.sensing.platform.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * This is the same as BasicAuthenticationEntryPoint but does not add the
 * "WWW-Authenticate" header to the response.
 * 
 * This means that the browser will not pop up a login dialog and the client
 * Javascript will get to see and handle the 401 code.
 * 
 */
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}
	
}