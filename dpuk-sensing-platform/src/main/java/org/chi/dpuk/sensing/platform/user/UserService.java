package org.chi.dpuk.sensing.platform.user;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;

public interface UserService {

	public User saveUser(User user);

	public User getUser(String userName);

	public User getUser(long userId);

	public void deleteUser(long userId);

	public List<User> getAllUsers();

	public List<User> getUsersByRole(User.Role role);

	public boolean doesUserExist(String userName);

	public Set<Study> getStudiesForUser(long userId);

	public Set<Study> getStudiesForUser(String userName);

	public User addStudyToUser(long studyId, long userId);

}