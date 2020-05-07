angular.module('study')

.factory('DeleteStudyService', [ '$http', function($http) {
	var service = {};

	service.deleteStudy = function(studyId) {
		var url = '/dpuk-sensing-platform/api/study/' + studyId;
		return $http['delete'](url).then(function(result) {
			return result.data;
		});
	};

	return service;
} ]);