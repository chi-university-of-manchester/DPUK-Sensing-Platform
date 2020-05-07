angular.module('user').controller('UserAddController', ['$scope', '$stateParams', 'UserService', 'HistoryService', function($scope, $stateParams, UserService, HistoryService) {
	$scope.user = new UserService();
	$scope.isFormSubmissionAttempted = false;
	$scope.userRoles = {availableOptions: [{id:'ROLE_ADMIN', name:'Administrator'},
	                                       {id:'ROLE_RESEARCHER_ADMIN', name:'Researcher administrator'},
	                                       {id:'ROLE_RESEARCHER', name:'Researcher'},
	                                       {id:'ROLE_SERVICE', name:'Service'}],
	                                       selectedOption: {id:'ROLE_RESEARCHER', name:'Researcher'} // The default value
	};

	$scope.save = function () {
		$scope.user.role = $scope.userRoles.selectedOption.id;
		$scope.isFormSubmissionAttempted = true;
		if ($scope.userForm.$valid) {
			$scope.user.$save(function(u, putResponseHeaders) {
				HistoryService.back();
			},
			function(putResponseHeaders) {
				alert('Unable to save your data. Please contact support.');
			});
		}
	};

	$scope.cancel = function () {
		HistoryService.back();
	};
	
}]);