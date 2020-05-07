angular.module('user').factory('UsersViewService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/user');
})