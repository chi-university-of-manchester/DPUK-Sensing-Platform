angular.module('dataSource').factory('DataSourceService', function($resource) {
	return $resource('/dpuk-sensing-platform/api/dataSource/:dataSourceId');
})