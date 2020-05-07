angular.module('participant').factory( 'AddStudyToParticipantService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/participant/addStudyToParticipant/:studyId/:participantId', {studyId: '@studyId', participantId: '@participantId'});
})