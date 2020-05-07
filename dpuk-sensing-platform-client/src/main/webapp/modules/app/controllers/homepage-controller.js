/*
 * This controller is used to route to the correct homepage for the current user.
 */
angular.module('app').controller('HomepageController', ['AuthenticationService', '$scope', '$state',
    function(AuthenticationService, $scope, $state) {

	// Every time the route '/' is shown we redirect to the correct home page for the currentUser.
	AuthenticationService.requestCurrentUser().then(function(currentUser) {
				
		if (currentUser) {
			$state.go('app.userStudies', {userId:currentUser.user.id, userName: currentUser.user.userName});
		}
		else {
			$state.go('app.signIn');
		}	
	});
}]);