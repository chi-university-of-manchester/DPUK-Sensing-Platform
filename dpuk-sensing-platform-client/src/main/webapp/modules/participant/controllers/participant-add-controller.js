angular.module('participant').controller('ParticipantAddController', ['$scope', '$stateParams', 'ParticipantService', 'AddStudyToParticipantService', 'HistoryService', function($scope, $stateParams, ParticipantService, AddStudyToParticipantService, HistoryService) {
	 $scope.formTitle = 'Add Participant';
	 $scope.participant = new ParticipantService();
	 $scope.isFormSubmissionAttempted = false; 	// For controlling validation on save
	 
	 $scope.save = function () {
		 $scope.isFormSubmissionAttempted = true;
		  if ($scope.participantForm.$valid) {
			$scope.participant.$save(function(u, putResponseHeaders) {
				AddStudyToParticipantService.save({studyId: $stateParams.studyId, participantId: u.id}, function(u, putResponseHeaders) {}, 
					function(putResponseHeaders) {
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
	 
	 $scope.cancel = function () {
		  HistoryService.back();
	 };
}]);