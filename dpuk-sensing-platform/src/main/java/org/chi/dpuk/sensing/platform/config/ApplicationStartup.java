package org.chi.dpuk.sensing.platform.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.chi.dpuk.sensing.platform.security.HashingUtility;
import org.chi.dpuk.sensing.platform.user.UserService;

/**
 * This class contains code to be called at application startup.
 */
@Component
public class ApplicationStartup {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ConfigurationService configurationService;

	@PostConstruct
	private void setupApplication() {
		if (BuildSettings.getInstance().getUseHibernate()) {
			addAdminUser();
			configurationService.configureDatabase();
		}
	}

	// Create the admin user if needed.
	private void addAdminUser() {
		if (userService.getUser("admin") == null) {
			User u = new User();
			u.setUserName("admin");
			String hashedPassword = HashingUtility.hashPassword("admin123");
			u.setPasswordHash(hashedPassword);
			u.setEmail("daniel.cave@manchester.ac.uk");
			u.setRole(Role.ROLE_ADMIN);
			u.setTitle("Mr");
			u.setFirstName("Admin");
			u.setLastName("User");
			userService.saveUser(u);
		}		
	}

}