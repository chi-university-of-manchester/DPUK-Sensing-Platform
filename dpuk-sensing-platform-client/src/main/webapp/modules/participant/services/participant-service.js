angular.module('participant').factory( 'ParticipantService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/participant/:participantId');
})