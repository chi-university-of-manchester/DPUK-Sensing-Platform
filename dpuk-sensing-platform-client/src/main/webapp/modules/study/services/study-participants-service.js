angular.module('study').factory( 'StudyParticipantsService', function($resource) {
		return $resource('/dpuk-sensing-platform/api/study/participant/:studyId');
})