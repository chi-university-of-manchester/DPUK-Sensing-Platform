angular.module('menu').controller('MenuController', ['$scope', '$state', 'AuthenticationService', function($scope, $state, AuthenticationService) {
	$scope.user = null;
	AuthenticationService.requestCurrentUser().then(function(currentUser) {		
		if (currentUser) {
			$scope.user = currentUser.user;
		} 
	});
}]);