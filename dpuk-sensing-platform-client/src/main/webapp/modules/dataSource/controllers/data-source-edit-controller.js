angular
		.module('dataSource')
		.controller(
				'DataSourceEditController',
				[
						'$scope',
						'$stateParams',
						'DataSourceService',
						'HistoryService',
						function($scope, $stateParams, DataSourceService,
								HistoryService) {
							DataSourceService.get({
								dataSourceId : $stateParams.dataSourceId
							}, function(dataSource) {
								$scope.dataSource = dataSource;
								$scope.formTitle = 'Edit Data Source: '
										+ $scope.dataSource.name;
							});

							$scope.isFormSubmissionAttempted = false; // For controlling validation on save

							$scope.save = function() {
								$scope.isFormSubmissionAttempted = true;
								if ($scope.dataSourceForm.$valid) {
									$scope.dataSource
											.$save(
													function(u,
															putResponseHeaders) {
														HistoryService.back();
													},
													function(putResponseHeaders) {
														// We need to add a general purpose message pop-up facility.
														alert('Unable to save your data. Please contact support.');
													});
								}
							};

							$scope.cancel = function() {
								HistoryService.back();
							};
						} ]);