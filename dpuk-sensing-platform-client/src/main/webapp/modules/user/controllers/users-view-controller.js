angular
.module('user')
.controller('UsersViewController',
		[
		 '$scope', 'UsersViewService', 'UtilityService', 'DeleteUserService',
		 function($scope, UsersViewService, UtilityService, DeleteUserService) {

			 $scope.update = function() {
				 UsersViewService.query({}, function (results) { $scope.users = results; });
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
		 }]);