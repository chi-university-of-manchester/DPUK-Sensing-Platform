angular
.module('study')
.controller('StudyUsersController',
		[
		 '$scope', 'StudyUsersService', 'UtilityService', 'DeleteUserService', '$stateParams',
		 function($scope, StudyUsersService, UtilityService, DeleteUserService, $stateParams) {

			 $scope.studyId = $stateParams.studyId
			 
			 $scope.studyName = $stateParams.studyName;

			 $scope.update = function() {
				 StudyUsersService.query({ studyId : $scope.studyId }, function(results) { 
					 $scope.users = results; 
				 });
			 }

			 $scope.deleteUser = function(id) {
				 UtilityService.showDeleteConfirmDialog('Delete User?', 'Are you sure you want to delete the user? This cannot be undone.')
				 .then( function(result) {
					 if (result) { // User selects 'Yes'
						 DeleteUserService.deleteUser(id).then(function() {
							 $scope.update();
						 });
					 }
				 });
			 };

			 $scope.update();
		 } ]);