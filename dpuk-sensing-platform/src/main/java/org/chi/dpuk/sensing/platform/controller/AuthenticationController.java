package org.chi.dpuk.sensing.platform.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.user.UserService;
import org.chi.dpuk.sensing.platform.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Used to establish a session from a client.
 */
@Controller
@RequestMapping("/api/authentication")
public class AuthenticationController {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

	/*
	 * Used for storing incoming login details.
	 */
	public static final class Credentials {
		private String userName;
		private String password;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	/*
	 * Defined in security.xml
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	/**
	 * Attempt to establish a session using the credentials in the json request
	 * body.
	 *
	 * The signIn method is accessible by anonymous users - see security.xml
	 *
	 * @param credentials
	 *            - contains the parsed JSON request body.
	 * @return the current signed in user information.
	 */
	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	@JsonView(View.User.class)
	public @ResponseBody Map<String, Object> signIn(@RequestBody Credentials credentials, HttpServletRequest request) {
		String userName = credentials.getUserName();
		String password = credentials.getPassword();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);

		try {
			Authentication auth = authenticationManager.authenticate(token);

			// Authentication was successful - so add the authentication to the
			// current session.

			// Create a session if non exists.
			request.getSession(true);

			// Set the authentication - which will be persisted in the session.
			SecurityContextHolder.getContext().setAuthentication(auth);
			log.info("Successful signIn for user: " + userName);
		} catch (BadCredentialsException e) {
			log.info("signIn attempt failed for user: " + userName);
		}
		return getCurrentUser();
	}

	/**
	 * Get the current user information, if any, from the current session, if
	 * any.
	 *
	 * This returns a json object containing the following:
	 *
	 * { user: the user if a session exists studyIds: the study ids associated
	 * with the user if the user is an admin }
	 *
	 * This is called on browser refreshes - which clear the client state.
	 *
	 * @return the filled in json object if the authentication is successful, an
	 *         empty object otherwise.
	 */
	@RequestMapping(value = "/currentUser", method = RequestMethod.GET)
	@JsonView(View.User.class)
	public @ResponseBody Map<String, Object> getCurrentUser() {

		Map<String, Object> result = new HashMap<String, Object>();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !auth.getName().equals("anonymousUser") && auth.isAuthenticated()) {
			User user = userService.getUser(auth.getName());
			if (user.getRole() != User.Role.ROLE_ADMIN) {
				// Expose Study information only for admin users
				user.setStudies(Collections.emptySet());
				log.info("No study information for user: " + user.getUserName() + " with role " + user.getRole().name());
			}
			result.put("user", user);
		}
		return result;
	}

	/**
	 * Invalidates the current session.
	 *
	 * We use a POST rather than a GET for the signOut.
	 *
	 * See: http://stackoverflow.com/questions/3521290/logout-get-or-post
	 */
	// Need to open this up to non-secure access.
	@RequestMapping(value = "/signOut", method = RequestMethod.POST)
	public void signOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		log.info("Successful signed out user");
	}

}
