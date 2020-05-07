package org.chi.dpuk.sensing.platform.controller;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.chi.dpuk.sensing.platform.dataSource.DataSourceService;
import org.chi.dpuk.sensing.platform.exception.DataSourceSameNameException;
import org.chi.dpuk.sensing.platform.exception.InvalidContactNumberException;
import org.chi.dpuk.sensing.platform.exception.InvalidNhsNumberException;
import org.chi.dpuk.sensing.platform.exception.ParticipantAddOrUpdateException;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.participant.ParticipantService;
import org.chi.dpuk.sensing.platform.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/api/participant")
public class ParticipantController {

	private static final String VALID_CONTACT_NUMBER_REGEX = "[0-9()+ ]{5,}";
	private static final String VALID_NHS_NUMBER_REGEX = "\\d{10}";

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private DataSourceService dataSourceService;

	@RequestMapping(method = RequestMethod.GET)
	@JsonView(View.Participant.class)
	public @ResponseBody List<Participant> getAllParticipants() {
		return participantService.getAllParticipants();
	}

	@RequestMapping(value = "/{participantId}", method = RequestMethod.GET)
	@JsonView(View.Participant.class)
	public @ResponseBody Participant getParticipant(@PathVariable Long participantId) {
		return participantService.getParticipant(participantId);
	}

	@RequestMapping(method = RequestMethod.GET, params = "participantName")
	@JsonView(View.Participant.class)
	public @ResponseBody List<Participant> getParticipants(@RequestParam String participantName) {
		return participantService.getParticipants(participantName);
	}

	// Specify an 'id' in the JSON to update, or omit 'id' to add.
	@RequestMapping(method = RequestMethod.POST)
	@JsonView(View.Participant.class)
	public @ResponseBody Participant addOrUpdateParticipant(@RequestBody Participant participant) {
		validateConatctNumber(participant);
		validateNhsNumber(participant);
		return participantService.addOrUpdateParticipant(participant);
	}

	private void validateConatctNumber(Participant participant) {
		if (participant == null) {
			throw new ParticipantAddOrUpdateException();
		}
		String contactNumber = participant.getContactNumber();
		if (contactNumber != null) {
			Pattern digitPattern = Pattern.compile(VALID_CONTACT_NUMBER_REGEX);
			if (!digitPattern.matcher(contactNumber).matches()) {
				throw new InvalidContactNumberException();
			}
		}
	}

	private void validateNhsNumber(Participant participant) {
		if (participant == null) {
			throw new ParticipantAddOrUpdateException();
		}
		String nhsNumber = participant.getNhsNumber();
		if (nhsNumber != null) {
			Pattern digitPattern = Pattern.compile(VALID_NHS_NUMBER_REGEX);
			if (!digitPattern.matcher(nhsNumber).matches()) {
				throw new InvalidNhsNumberException();
			}
		}
	}

	@RequestMapping(value = "/dataSource/{participantId}", method = RequestMethod.GET)
	@JsonView(View.DataSource.class)
	public @ResponseBody Set<DataSource> getDataSourcesForParticipant(@PathVariable Long participantId) {
		return participantService.getDataSourcesForParticipant(participantId);
	}

	@ResponseBody
	@RequestMapping(value = "/addDataSourceToParticipant/{dataSourceId}/{participantId}", method = RequestMethod.POST)
	@JsonView(View.Participant.class)
	public Participant addDataSourceToParticipant(@PathVariable Long dataSourceId, @PathVariable Long participantId) {
		if (validateDataSourceWithTheSameNameCanBeAddedToTheSameParticipant(participantId, dataSourceId)) {
			// delete stale dataSource
			dataSourceService.deleteDataSource(dataSourceId);
			throw new DataSourceSameNameException();
		}
		return participantService.addDataSourceToParticipant(dataSourceId, participantId);
	}

	private boolean validateDataSourceWithTheSameNameCanBeAddedToTheSameParticipant(Long participantId,
			Long dataSourceId) {
		if (participantId == null || dataSourceId == null) {
			throw new DataSourceSameNameException();
		}
		Set<DataSource> dataSourcesForParticipant = participantService.getDataSourcesForParticipant(participantId);
		DataSource dataSource = dataSourceService.getDataSource(dataSourceId);
		if (dataSourcesForParticipant == null || dataSource == null) {
			throw new DataSourceSameNameException();
		}

		boolean doesDataSourceWithSameNameExist = false;

		for (DataSource dataSourceForParticipant : dataSourcesForParticipant) {
			if (dataSource.getName().equalsIgnoreCase(dataSourceForParticipant.getName())) {
				doesDataSourceWithSameNameExist = true;
				break;
			}
		}

		return doesDataSourceWithSameNameExist;
	}

	@ResponseBody
	@RequestMapping(value = "/addDataSourcesToParticipant/{dataSourceIds}/{participantId}", method = RequestMethod.POST)
	@JsonView(View.Participant.class)
	public Participant addDataSourcesToParticipant(@PathVariable List<Long> dataSourceIds,
			@PathVariable Long participantId) {
		Participant participant = null;
		for (Long dataSourceId : dataSourceIds) {
			participant = participantService.addDataSourceToParticipant(dataSourceId, participantId);
		}
		return participant;
	}

	@JsonView(View.Study.class)
	@RequestMapping(value = "/study/{participantId}", method = RequestMethod.GET)
	public @ResponseBody Study getStudyForParticipant(@PathVariable Long participantId) {
		return participantService.getStudyForParticipant(participantId);
	}

	@JsonView(View.Participant.class)
	@ResponseBody
	@RequestMapping(value = "/addStudyToParticipant/{studyId}/{participantId}", method = RequestMethod.POST)
	public Participant addStudyToParticipant(@PathVariable Long studyId, @PathVariable Long participantId) {
		return participantService.addStudyToParticipant(studyId, participantId);
	}

	@RequestMapping(value = "/{participantId}", method = RequestMethod.DELETE)
	@JsonView(View.Participant.class)
	public @ResponseBody Participant deleteParticipant(@PathVariable Long participantId) /*
																							 * throws
																							 * ForbiddenAccessException
																							 */ {
		Participant participant = participantService.getParticipant(participantId);

		if (participant != null) {
			// TODO: Determine if user is authorised to delete participant here.
			participant.setStudy(null); // Detach the study from the participant
			participantService.addOrUpdateParticipant(participant);// Persist it
			participantService.deleteParticipant(participantId); // Delete it
		}

		return participant;
	}

}