angular
.module('participant')
.controller('ParticipantDataSourcesController',
		[
		 '$scope', 'ParticipantDataSourcesService', 'UtilityService', 'DeleteDataSourceService', '$stateParams',
		 function($scope, ParticipantDataSourcesService, UtilityService, DeleteDataSourceService,  $stateParams) {

			 $scope.participantName = $stateParams.participantName;

			 $scope.participantId = $stateParams.participantId;

			 $scope.update = function() {
				 ParticipantDataSourcesService.query({ participantId : $scope.participantId }, function(results) { 
					 $scope.dataSources = results; 
				 });
			 }

			 $scope.deleteDataSource = function(id) {
				 UtilityService.showDeleteConfirmDialog('Delete Data Source?',
				 'Are you sure you want to delete the data source? All associated data will be removed. This cannot be undone.')
				 .then( function(result) {
					 if (result) { // User selects 'Yes'
						 DeleteDataSourceService.deleteDataSource(id).then(function() {
							 $scope.update();
						 });
					 }
				 });
			 };

			 $scope.update();
		 } ]);