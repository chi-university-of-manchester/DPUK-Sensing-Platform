package org.chi.dpuk.sensing.platform.user;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.User;

public interface UserDAO {

	public User saveUser(User user);

	public User getUser(String userName);

	public User getUser(long userId);

	public void deleteUser(long userId);

	public List<User> getAllUsers();

	public List<User> getUsersByRole(User.Role role);

}