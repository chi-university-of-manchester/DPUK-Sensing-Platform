angular.module('user').controller('UserEditController', ['$scope', '$stateParams', 'UserService', 'HistoryService', function($scope, $stateParams, UserService, HistoryService) {
	UserService.get({userId: $stateParams.userId}, function (user) {
		$scope.user = user;
		$scope.formTitle = 'Editing User: ' + $scope.user.userName;
	});

	$scope.isFormSubmissionAttempted = false; // For controlling validation on save

	$scope.save = function () {
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