angular
		.module('common')
		.factory(
				'fileUpload',
				[
						'$http',
						'$stateParams',
						'HistoryService',
						function($http, $stateParams, HistoryService) {
							// The public API of the service
							var factory = {
								uploadFileToUrl : function(file, startDate,
										endDate, uploadUrl) {
									var formData = new FormData();
									formData.append('file', file);
									formData.append('startDate', startDate);
									formData.append('endDate', endDate);

									$http
											.post(
													uploadUrl,
													formData,
													{
														transformRequest : angular.identity,
														headers : {
															'Content-Type' : undefined
														}
													})
											.success(function() {

												HistoryService.back();

											})
											.error(
													function() {
														// We need to add a general purpose message pop-up facility.
														alert('Unable to save your data. Please contact support.');
													});
								}
							};
							return factory;
						} ]);