angular
.module('user')
.controller(
		'UserStudiesController',
		[
		 '$scope', 'UserStudiesService', 'UsersViewService', 'UtilityService', 'DeleteStudyService', '$stateParams',
		 function($scope, UserStudiesService, UsersViewService, UtilityService, DeleteStudyService, $stateParams) {

			 $scope.userId = $stateParams.userId;

			 $scope.userName = $stateParams.userName;

			 $scope.update = function(){
				 UserStudiesService.query({userId : $scope.userId}, function(results) { 
					 $scope.studies = results;
				 });			 
			 }			 

			 $scope.deleteStudy = function(id) {
				 UtilityService.showDeleteConfirmDialog('Delete Study?',
				 'Are you sure you want to delete the study?  All associated participants and data will be removed. This cannot be undone.')
				 .then(function(result) {
					 if (result) { // User selects 'Yes'
						 DeleteStudyService.deleteStudy(id).then(function() {
							 $scope.update();
						 });
					 }
				 });
			 };

			 $scope.update();
		 } ]);