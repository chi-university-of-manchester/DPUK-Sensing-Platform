package org.chi.dpuk.sensing.platform.participant;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.Participant;

public interface ParticipantDAO {

	public Participant addOrUpdateParticipant(Participant participant);

	public Participant getParticipant(long participantId);

	public void deleteParticipant(long participantId);

	public List<Participant> getParticipants(String name);

	public List<Participant> getAllParticipants();

}
