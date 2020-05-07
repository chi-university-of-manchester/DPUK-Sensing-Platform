angular.module('dataSource')

.factory('DeleteDataSourceService', [ '$http', function($http) {
	var service = {};

	service.deleteDataSource = function(dataSourceId) {
		var url = '/dpuk-sensing-platform/api/dataSource/' + dataSourceId;
		return $http['delete'](url).then(function(result) {
			return result.data;
		});
	};

	return service;
} ]);