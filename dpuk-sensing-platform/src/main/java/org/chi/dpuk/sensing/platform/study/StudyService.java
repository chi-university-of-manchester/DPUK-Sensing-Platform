package org.chi.dpuk.sensing.platform.study;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;

public interface StudyService {

	public Study addOrUpdateStudy(Study study);

	public Study getStudy(String name);

	public Study getStudy(long id);

	public List<Study> getAllStudies();

	public boolean doesStudyExist(String name);

	public Set<User> getUsersForStudy(String name);

	public Set<User> getUsersForStudy(long id);

	public Study addUserToStudy(long userId, long studyId);

	public Set<Participant> getParticipantsForStudy(String name);

	public Set<Participant> getParticipantsForStudy(long studyId);

	public Study addParticipantToStudy(long participantId, long studyId);

	public void deleteStudy(long studyId);

}