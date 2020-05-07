package org.chi.dpuk.sensing.platform.participant;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;

public interface ParticipantService {

	public Participant addOrUpdateParticipant(Participant participant);

	public Participant getParticipant(long participantId);

	public void deleteParticipant(long participantId);

	public List<Participant> getParticipants(String name);

	public List<Participant> getAllParticipants();

	public Study getStudyForParticipant(long participantId);

	public Participant addStudyToParticipant(long studyId, long participantId);

	public Set<DataSource> getDataSourcesForParticipant(long participantId);

	public Participant addDataSourceToParticipant(long dataSourceId, long participantId);

}