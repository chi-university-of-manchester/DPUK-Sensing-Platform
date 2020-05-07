angular.module('participant')

.factory('DeleteParticipantService', [ '$http', function($http) {
	var service = {};

	service.deleteParticipant = function(participantId) {
		var url = '/dpuk-sensing-platform/api/participant/' + participantId;
		return $http['delete'](url).then(function(result) {
			return result.data;
		});
	};

	return service;
} ]);