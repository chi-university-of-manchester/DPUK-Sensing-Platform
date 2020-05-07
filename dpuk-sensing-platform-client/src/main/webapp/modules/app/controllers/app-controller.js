/* 
 * The overall app controller whose $scope is inherited by all the other controllers.
 * See the <html> element in index.html
 * This controller is executed on app startup i.e. every time the browser is refreshed.
 */
angular.module('app').controller(
		'AppController',
		[
				'$rootScope',
				'$scope',
				'AuthenticationService',
				'$state',
				'$http',
				function($rootScope, $scope, AuthenticationService, $state,
						$http) {

					// Get the currentUser - used to display the current user in the navbar
					$scope.currentUser = AuthenticationService
							.requestCurrentUser();

					// Watch for changes in the currentUser
					$scope.$watch(function() {
						return AuthenticationService.currentUser;
					}, function(currentUser) {
						$scope.currentUser = currentUser;
					});

					// signOut the current user and redirect to 'signIn' when done.
					$scope.signOut = function() {
						AuthenticationService.signOut().then(function() {
							$state.go('app.signIn');
						});
					};
				} ]);