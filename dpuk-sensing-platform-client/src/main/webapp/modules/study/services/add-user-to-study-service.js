angular.module('study').factory( 'AddUserToStudyService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/study/addUserToStudy/:userId/:studyId', {userId: '@userId', studyId: '@studyId'});
})