package org.chi.dpuk.sensing.platform.controller;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.study.StudyService;
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
@RequestMapping("/api/study")
public class StudyController {

	@Autowired
	private StudyService studyService;

	@JsonView(View.Study.class)
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Study> getAllStudies() {
		return studyService.getAllStudies();
	}

	@JsonView(View.Study.class)
	@RequestMapping(value = "/{studyId}", method = RequestMethod.GET)
	public @ResponseBody Study getStudy(@PathVariable Long studyId) {
		return studyService.getStudy(studyId);
	}

	@JsonView(View.Study.class)
	@RequestMapping(method = RequestMethod.GET, params = "studyName")
	public @ResponseBody Study getStudy(@RequestParam String studyName) {
		return studyService.getStudy(studyName);
	}

	// Specify an 'id' in the JSON to update, or omit 'id' to add.
	@JsonView(View.Study.class)
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Study addOrUpdateStudy(@RequestBody Study study) {
		return studyService.addOrUpdateStudy(study);
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET, params = "studyName")
	@JsonView(View.User.class)
	public @ResponseBody Set<User> getUsersForStudy(@RequestParam String studyName) {
		return studyService.getUsersForStudy(studyName);
	}

	@RequestMapping(value = "/user/{studyId}", method = RequestMethod.GET)
	@JsonView(View.User.class)
	public @ResponseBody Set<User> getUsersForStudy(@PathVariable Long studyId) {
		return studyService.getUsersForStudy(studyId);
	}

	@ResponseBody
	@RequestMapping(value = "/addUserToStudy/{userId}/{studyId}", method = RequestMethod.POST)
	@JsonView(View.Study.class)
	public Study addUserToStudy(@PathVariable Long userId, @PathVariable Long studyId) {
		return studyService.addUserToStudy(userId, studyId);
	}

	@ResponseBody
	@RequestMapping(value = "/addUsersToStudy/{userIds}/{studyId}", method = RequestMethod.POST)
	@JsonView(View.Study.class)
	public Study addUsersToStudy(@PathVariable List<Long> userIds, @PathVariable Long studyId) {
		Study study = null;
		for (Long userId : userIds) {
			study = studyService.addUserToStudy(userId, studyId);
		}
		return study;
	}

	@RequestMapping(value = "/participant", method = RequestMethod.GET, params = "studyName")
	@JsonView(View.Participant.class)
	public @ResponseBody Set<Participant> getParticipantsForStudy(@RequestParam String studyName) {
		return studyService.getParticipantsForStudy(studyName);
	}

	@RequestMapping(value = "/participant/{studyId}", method = RequestMethod.GET)
	@JsonView(View.Participant.class)
	public @ResponseBody Set<Participant> getParticipantsForStudy(@PathVariable Long studyId) {
		return studyService.getParticipantsForStudy(studyId);
	}

	@ResponseBody
	@RequestMapping(value = "/addParticipantToStudy/{participantId}/{studyId}", method = RequestMethod.POST)
	@JsonView(View.Study.class)
	public Study addParticipantToStudy(@PathVariable Long participantId, @PathVariable Long studyId) {
		return studyService.addParticipantToStudy(participantId, studyId);
	}

	@ResponseBody
	@RequestMapping(value = "/addParticipantsToStudy/{participantIds}/{studyId}", method = RequestMethod.POST)
	@JsonView(View.Study.class)
	public Study addParticipantsToStudy(@PathVariable List<Long> participantIds, @PathVariable Long studyId) {
		Study study = null;
		for (Long participantId : participantIds) {
			study = studyService.addParticipantToStudy(participantId, studyId);
		}
		return study;
	}

	@RequestMapping(value = "/doesStudyExist", method = RequestMethod.GET, params = "name")
	@JsonView(View.Study.class)
	public @ResponseBody Boolean doesStudyExist(@RequestParam String name) {
		return studyService.doesStudyExist(name);
	}
	
	@RequestMapping(value = "/{studyId}", method = RequestMethod.DELETE)
	@JsonView(View.Study.class)
	public @ResponseBody Study deleteStudy(@PathVariable Long studyId) /* throws ForbiddenAccessException */ {
		Study study = studyService.getStudy(studyId);

		if (study != null) {
			// TODO: Determine if user is authorised to delete study here.
			studyService.deleteStudy(studyId); // Delete it
		}

		return study;
	}
}