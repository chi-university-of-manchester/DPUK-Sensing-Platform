angular.module('dataSet')

.factory('DeleteDataSetService', [ '$http', function($http) {
	var service = {};

	service.deleteDataSet = function(dataSetId) {
		var url = '/dpuk-sensing-platform/api/dataSet/' + dataSetId;
		return $http['delete'](url).then(function(result) {
			return result.data;
		});
	};

	return service;
} ]);