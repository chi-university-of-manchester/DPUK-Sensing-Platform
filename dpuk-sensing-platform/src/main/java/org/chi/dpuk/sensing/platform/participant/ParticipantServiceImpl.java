package org.chi.dpuk.sensing.platform.participant;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.dataSource.DataSourceDAO;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.study.StudyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

	@Autowired
	private ParticipantDAO participantDAO;

	@Autowired
	private StudyDAO studyDAO;

	@Autowired
	private DataSourceDAO dataSourceDAO;

	@Override
	public Participant addOrUpdateParticipant(Participant participant) {
		return participantDAO.addOrUpdateParticipant(participant);
	}

	@Override
	public Participant getParticipant(long participantId) {
		return participantDAO.getParticipant(participantId);
	}

	@Override
	public void deleteParticipant(long participantId) {
		participantDAO.deleteParticipant(participantId);
	}

	@Override
	public List<Participant> getParticipants(String name) {
		return participantDAO.getParticipants(name);
	}

	@Override
	public List<Participant> getAllParticipants() {
		return participantDAO.getAllParticipants();
	}

	@Override
	public Set<DataSource> getDataSourcesForParticipant(long participantId) {
		return this.getParticipant(participantId).getDataSources();
	}

	@Override
	public Participant addDataSourceToParticipant(long dataSourceId, long participantId) {
		DataSource dataSource = dataSourceDAO.getDataSource(dataSourceId);
		Participant participant = this.getParticipant(participantId);
		participant.addDataSource(dataSource);
		dataSource.setParticipant(participant);
		return this.addOrUpdateParticipant(participant);
	}

	public Study getStudyForParticipant(long participantId) {
		return this.getParticipant(participantId).getStudy();
	}

	@Override
	public Participant addStudyToParticipant(long studyId, long participantId) {
		Study study = studyDAO.getStudy(studyId);
		Participant participant = this.getParticipant(participantId);
		participant.setStudy(study);
		return this.addOrUpdateParticipant(participant);
	}

}
