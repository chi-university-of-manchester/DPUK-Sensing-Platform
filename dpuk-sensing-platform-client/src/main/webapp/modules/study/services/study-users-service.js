angular.module('study').factory( 'StudyUsersService', function($resource) {
		return $resource('/dpuk-sensing-platform/api/study/user/:studyId');
})