angular
		.module('dataSource')
		.controller(
				'AddDataSetToDataSourceController',
				[
						'$scope',
						'fileUpload',
						'$stateParams',
						'HistoryService',
						function($scope, fileUpload, $stateParams,
								HistoryService) {
							$scope.formTitle = 'Add Dataset to Datasource';
							$scope.isFormSubmissionAttempted = false; // For controlling validation on save
							var startDate = $scope.startDate;
							var endDate = $scope.endDate;
							$scope.today = function() {
								$scope.startDate = new Date();
								$scope.endDate = new Date();
							};
							$scope.today();

							$scope.toggleMin = function() {
								$scope.minDate = $scope.minDate ? null
										: new Date();
							};

							$scope.toggleMin();

							$scope.startDateConfig = {
								opened : false,
								open : function() {
									$scope.startDateConfig.opened = true;
								}
							};

							$scope.endDateConfig = {
								opened : false,
								open : function() {
									$scope.endDateConfig.opened = true;
								}
							};

							$scope.save = function() {
								$scope.isFormSubmissionAttempted = true;
								var file = $scope.file;
								var startDate = $scope.startDate;
								var endDate = $scope.endDate;
								if (moment(startDate).diff(moment(endDate)) > 0) {
									$scope.isFormSubmissionAttempted = false;
									alert('The start date cannot be in the future.');
									return;
								}
								if (typeof file == 'undefined') {
									$scope.isFormSubmissionAttempted = false;
									alert('Please select a file to upload.');
									return;
								}
								var uploadUrl = '/dpuk-sensing-platform/api/dataSource/addDataSetToDataSource/'
										+ $stateParams.dataSourceId;
								fileUpload.uploadFileToUrl(file, startDate,
										endDate, uploadUrl);
							};

							$scope.cancel = function() {
								HistoryService.back();
							};
						} ]);
