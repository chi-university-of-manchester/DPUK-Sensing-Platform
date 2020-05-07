angular.module('dataSet').factory(
		'DownloadDataSetService',
		[
				'$http',
				function($http) {
					return {
						downloadDataSet : function(dataSetId) {
							return $http.get(
									'/dpuk-sensing-platform/api/dataSet/download/'
											+ dataSetId, {
										responseType : 'arraybuffer',
									})
									.then(
											function(response, status, headers,
													config) {
												return response;
											});
						},
					};
				} ])