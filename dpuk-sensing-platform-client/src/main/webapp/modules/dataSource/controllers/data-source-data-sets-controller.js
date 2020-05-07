angular
.module('dataSource')
.controller('DataSourceDataSetsController',
		[ 
		 '$scope', 'DataSourceDataSetsService', 'UtilityService', 'DeleteDataSetService', 'DownloadDataSetService', '$stateParams',
		 function($scope, DataSourceDataSetsService, UtilityService, DeleteDataSetService, DownloadDataSetService, $stateParams) {

			 $scope.dataSourceName = $stateParams.dataSourceName;

			 $scope.dataSourceId = $stateParams.dataSourceId;

			 $scope.update = function() {
				 DataSourceDataSetsService.query({dataSourceId : $scope.dataSourceId}, function(results) { 
					 $scope.dataSets = results; 
				 });
			 }

			 $scope.deleteDataSet = function(id) {
				 UtilityService.showDeleteConfirmDialog('Delete Data Set?', 'Are you sure you want to delete the data set? This cannot be undone.')
				 .then( function(result) {
					 if (result) { // User selects 'Yes'
						 DeleteDataSetService.deleteDataSet(id).then(function() {
							 $scope.update();
						 }); 
					 } 
				 });
			 };

			 $scope.update();
			 
			 $scope.downloadDataSet = function(id)  {
				 var downloadLink = document.createElement('a');
				 document.body.appendChild(downloadLink);
				 downloadLink.style = 'display: none';	
				 DownloadDataSetService.downloadDataSet(id).then(function (response) {
					 var fileName = response.headers('file-name');
					 var file = new Blob([response.data], {type: 'application/*'});
					 var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
					 downloadLink.href = fileURL;
					 downloadLink.download = fileName;
					 downloadLink.click();
				});	 
			 };
		 } ]);