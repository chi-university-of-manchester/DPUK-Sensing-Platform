angular.module('study').controller('StudyAddController', ['$scope', '$stateParams', 'StudyService', 'AddUserToStudyService', 'HistoryService', function($scope, $stateParams, StudyService, AddUserToStudyService, HistoryService) {
	$scope.formTitle = 'Add Study';
	$scope.study = new StudyService();
	$scope.isFormSubmissionAttempted = false; // For controlling validation on save

	$scope.save = function () {
		$scope.isFormSubmissionAttempted = true;
		if ($scope.studyForm.$valid) {
			$scope.study.$save(function(u, putResponseHeaders) {
				AddUserToStudyService.save({userId: $stateParams.userId, studyId: u.id}, function(u, putResponseHeaders) {}, 
						function(putResponseHeaders) {
					// We need to add a general purpose message pop-up facility.
					alert('Unable to save your data. Please contact support.');
				}).$promise.then(function() {
					HistoryService.back()
				});
			},
			function(putResponseHeaders) {
				// We need to add a general purpose message pop-up facility.
				alert('Unable to save your data. Please contact support.');
			});
		}
	};

	$scope.cancel = function () {
		HistoryService.back();
	};
}]);