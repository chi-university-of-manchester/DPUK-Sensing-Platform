angular.module('participant').controller('ParticipantEditController', ['$scope', '$stateParams', 'ParticipantService', 'HistoryService', function($scope, $stateParams, ParticipantService, HistoryService) {
	 ParticipantService.get({participantId: $stateParams.participantId}, function (participant) {
		$scope.participant = participant;
		$scope.formTitle = 'Edit Participant: ' + $scope.participant.name;
	 });
	 
	 $scope.isFormSubmissionAttempted = false; 	// For controlling validation on save
	 
	 $scope.save = function () {
		 $scope.isFormSubmissionAttempted = true;
		  if ($scope.participantForm.$valid) {
			$scope.participant.$save(function(u, putResponseHeaders) {
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