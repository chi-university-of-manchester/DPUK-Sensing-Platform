angular.module('user').factory('UserService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/user/:userId');
})