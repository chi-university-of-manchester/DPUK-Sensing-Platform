angular.module('study').controller('StudyEditController', ['$scope', '$stateParams', 'StudyService', 'HistoryService', function($scope, $stateParams, StudyService, HistoryService) {
	 StudyService.get({studyId: $stateParams.studyId}, function (study) {
		$scope.study = study;
		$scope.formTitle = 'Edit Study: ' + $scope.study.name;
	 });
	 
	 $scope.isFormSubmissionAttempted = false; 	// For controlling validation on save
	 
	 $scope.save = function () {
		 $scope.isFormSubmissionAttempted = true;
		  if ($scope.studyForm.$valid) {
			$scope.study.$save(function(u, putResponseHeaders) {
				HistoryService.back();
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