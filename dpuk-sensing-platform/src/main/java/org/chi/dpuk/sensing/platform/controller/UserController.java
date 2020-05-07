package org.chi.dpuk.sensing.platform.controller;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.user.UserService;
import org.chi.dpuk.sensing.platform.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@JsonView(View.User.class)
	public @ResponseBody List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@JsonView(View.User.class)
	public @ResponseBody User getUser(@PathVariable Long userId) {
		return userService.getUser(userId);
	}

	@RequestMapping(method = RequestMethod.GET, params = "userName")
	@JsonView(View.User.class)
	public @ResponseBody User getUser(@RequestParam String userName) {
		return userService.getUser(userName);
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(View.User.class)
	public @ResponseBody User addUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	@RequestMapping(method = RequestMethod.GET, params = "role")
	@JsonView(View.User.class)
	public @ResponseBody List<User> getUsersByRole(@RequestParam String role) {
		return userService.getUsersByRole(User.Role.valueOf(role));
	}

	@RequestMapping(value = "/study", method = RequestMethod.GET, params = "userName")
	@JsonView(View.Study.class)
	public @ResponseBody Set<Study> getStudiesForUser(@RequestParam String userName) {
		return userService.getStudiesForUser(userName);
	}

	@RequestMapping(value = "/study/{userId}", method = RequestMethod.GET)
	@JsonView(View.Study.class)
	public @ResponseBody Set<Study> getStudiesForUser(@PathVariable Long userId) {
		return userService.getStudiesForUser(userId);
	}

	@ResponseBody
	@RequestMapping(value = "/addStudyToUser/{studyId}/{userId}", method = RequestMethod.POST)
	@JsonView(View.User.class)
	public User addStudyToUser(@PathVariable Long studyId, @PathVariable Long userId) {
		return userService.addStudyToUser(studyId, userId);
	}

	@ResponseBody
	@RequestMapping(value = "/addStudiesToUser/{studyIds}/{userId}", method = RequestMethod.POST)
	@JsonView(View.User.class)
	public User addStudiesToUser(@PathVariable List<Long> studyIds, @PathVariable Long userId) {
		User user = null;
		for (Long studyId : studyIds) {
			user = userService.addStudyToUser(studyId, userId);
		}
		return user;
	}

	@RequestMapping(value = "/doesUserExist", method = RequestMethod.GET, params = "userName")
	@JsonView(View.User.class)
	public @ResponseBody Boolean doesUserExist(@RequestParam String userName) {
		return userService.doesUserExist(userName);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@JsonView(View.User.class)
	public @ResponseBody User deleteUser(
			@PathVariable Long userId) /* throws ForbiddenAccessException */ {

		User userToDelete = userService.getUser(userId);
		User currentUser = getCurrentUser();

		if (userToDelete != null && currentUser != null && !isDeleteForCurrentUser(userToDelete, currentUser)) {
			// TODO: Determine if user is authorised to delete the user here.
			userService.deleteUser(userId);
		}

		return userToDelete;
	}

	private User getCurrentUser() {
		User currentUser = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !auth.getName().equals("anonymousUser") && auth.isAuthenticated()) {
			currentUser = userService.getUser(auth.getName());
		}
		return currentUser;
	}

	private boolean isDeleteForCurrentUser(User userToDelete, User currentUser) {
		return (userToDelete.getId().longValue() == getCurrentUser().getId().longValue());
	}
}