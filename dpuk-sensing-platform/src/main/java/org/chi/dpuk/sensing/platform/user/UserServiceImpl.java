package org.chi.dpuk.sensing.platform.user;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.chi.dpuk.sensing.platform.study.StudyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private StudyDAO studyDAO;

	@Override
	public User getUser(String userName) {
		return userDAO.getUser(userName);
	}

	@Override
	public User saveUser(User user) {
		return userDAO.saveUser(user);
	}

	@Override
	public User getUser(long userId) {
		return userDAO.getUser(userId);
	}

	@Override
	public void deleteUser(long userId) {
		userDAO.deleteUser(userId);
	}

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Override
	public boolean doesUserExist(String userName) {
		return this.getUser(userName) != null ? false : true;
	}

	@Override
	public List<User> getUsersByRole(Role role) {
		return userDAO.getUsersByRole(role);
	}

	@Override
	public Set<Study> getStudiesForUser(String userName) {
		return this.getUser(userName).getStudies();
	}

	@Override
	public Set<Study> getStudiesForUser(long userId) {
		return this.getUser(userId).getStudies();
	}

	@Override
	public User addStudyToUser(long studyId, long userId) {
		Study study = studyDAO.getStudy(studyId);
		User user = this.getUser(userId);
		user.addStudy(study);
		return this.saveUser(user);
	}

}