angular
		.module('dataSource')
		.factory(
				'DataSourceDataSetsService',
				function($resource) {
					return $resource('/dpuk-sensing-platform/api/dataSource/dataSet/:dataSourceId');
				})