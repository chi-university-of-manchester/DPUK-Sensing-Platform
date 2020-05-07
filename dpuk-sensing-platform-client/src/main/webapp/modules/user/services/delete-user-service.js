angular.module('user')

.factory('DeleteUserService', [ '$http', function($http) {
	var service = {};

	service.deleteUser = function(userId) {
		var url = '/dpuk-sensing-platform/api/user/' + userId;
		return $http['delete'](url).then(function(result) {
			return result.data;
		});
	};

	return service;
} ]);