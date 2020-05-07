angular.module('user').factory('UserStudiesService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/user/study/:userId');
})