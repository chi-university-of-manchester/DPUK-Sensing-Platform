package org.chi.dpuk.sensing.platform.user;

import java.util.ArrayList;
import java.util.List;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Used by Authentication Manager to lookup the user by username and authorities by role.
 */

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		org.chi.dpuk.sensing.platform.model.User user = userDAO.getUser(userName);

		if (user == null)
			throw new UsernameNotFoundException("No user with username: " + userName);

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		return new User(user.getUserName(),
						user.getPasswordHash(),
						enabled,
						accountNonExpired,
						credentialsNonExpired,
						accountNonLocked,
						getGrantedAuthorities(user.getRole()));
	}

	public static List<GrantedAuthority> getGrantedAuthorities(Role role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		return authorities;
	}

}