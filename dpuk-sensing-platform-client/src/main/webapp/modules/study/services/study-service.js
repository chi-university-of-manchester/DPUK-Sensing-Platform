angular.module('study').factory( 'StudyService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/study/:studyId');
})