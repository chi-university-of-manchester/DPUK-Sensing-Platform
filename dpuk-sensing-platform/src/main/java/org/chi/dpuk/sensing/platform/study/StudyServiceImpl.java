package org.chi.dpuk.sensing.platform.study;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.participant.ParticipantDAO;
import org.chi.dpuk.sensing.platform.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudyServiceImpl implements StudyService {

	@Autowired
	private StudyDAO studyDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ParticipantDAO participantDAO;

	public Study addOrUpdateStudy(Study study) {
		return studyDAO.addOrUpdateStudy(study);
	}

	@Override
	public Study getStudy(String name) {
		return studyDAO.getStudy(name);
	}

	@Override
	public Study getStudy(long id) {
		return studyDAO.getStudy(id);
	}

	@Override
	public List<Study> getAllStudies() {
		return studyDAO.getAllStudies();
	}

	@Override
	public boolean doesStudyExist(String name) {
		return this.getStudy(name) != null ? false : true;
	}

	@Override
	public Set<User> getUsersForStudy(String name) {
		return this.getStudy(name).getUsers();
	}

	@Override
	public Set<User> getUsersForStudy(long id) {
		return this.getStudy(id).getUsers();
	}

	@Override
	public Study addUserToStudy(long userId, long studyId) {
		User user = userDAO.getUser(userId);
		Study study = this.getStudy(studyId);
		study.addUser(user);
		return this.addOrUpdateStudy(study);
	}

	@Override
	public Set<Participant> getParticipantsForStudy(String name) {
		return this.getStudy(name).getParticipants();
	}

	@Override
	public Set<Participant> getParticipantsForStudy(long studyId) {
		return this.getStudy(studyId).getParticipants();
	}

	@Override
	public Study addParticipantToStudy(long participantId, long studyId) {
		Participant participant = participantDAO.getParticipant(participantId);
		Study study = this.getStudy(studyId);
		study.addParticipant(participant);
		participant.setStudy(study);
		return this.addOrUpdateStudy(study);
	}

	@Override
	public void deleteStudy(long studyId) {
		studyDAO.deleteStudy(studyId);
	}

}