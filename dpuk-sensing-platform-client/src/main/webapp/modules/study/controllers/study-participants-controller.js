angular
.module('study')
.controller(
		'StudyParticipantsController',
		[
		 '$scope', 'StudyParticipantsService', 'UtilityService', 'DeleteParticipantService', '$stateParams',
		 function($scope, StudyParticipantsService, UtilityService, DeleteParticipantService, $stateParams) {

			 $scope.studyName = $stateParams.studyName;

			 $scope.studyId = $stateParams.studyId;

			 $scope.update = function() {
				 StudyParticipantsService.query({studyId : $scope.studyId}, function(results) {
					 $scope.participants = results;
				 });
			 }

			 $scope.deleteParticipant = function(id) {
				 UtilityService.showDeleteConfirmDialog('Delete Participant?',
				 'Are you sure you want to delete the participant?  All associated data will be removed and cannot be undone.')
				 .then( function(result) {
					 if (result) { // User selects 'Yes' 
						 DeleteParticipantService.deleteParticipant(id).then(function() {
							 $scope.update();
						 });
					 }
				 });
			 };

			 $scope.update();
		 } ]);