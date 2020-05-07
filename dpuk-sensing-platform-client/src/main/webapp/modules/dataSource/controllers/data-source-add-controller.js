angular
		.module('dataSource')
		.controller(
				'DataSourceAddController',
				[
						'$scope',
						'$stateParams',
						'DataSourceService',
						'AddParticipantToDataSourceService',
						'HistoryService',
						function($scope, $stateParams, DataSourceService,
								AddParticipantToDataSourceService,
								HistoryService) {
							$scope.formTitle = 'Add Data Source';
							$scope.dataSource = new DataSourceService();
							$scope.isFormSubmissionAttempted = false; // For controlling validation on save

							$scope.save = function() {
								$scope.isFormSubmissionAttempted = true;
								if ($scope.dataSourceForm.$valid) {
									$scope.dataSource
											.$save(
													function(u,
															putResponseHeaders) {
														AddParticipantToDataSourceService
																.save(
																		{
																			participantId : $stateParams.participantId,
																			dataSourceId : u.id
																		},
																		function(
																				u,
																				putResponseHeaders) {
																		},
																		function(
																				putResponseHeaders) {
																			// We need to add a general purpose message pop-up facility.
																			alert('Unable to save your data. Please contact support.');
																		});
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