angular
		.module('authentication')
		.controller(
				'AuthenticationController',
				[
						'$scope',
						'$state',
						'AuthenticationService',
						function($scope, $state, AuthenticationService) {
							// The credentials model for this form i.e. userName and password.
							$scope.user = {};
							$scope.user.userName = 'researcher';
							$scope.user.password = 'admin123';

							// Any error message from failing to signIn
							$scope.authError = null;

							// Attempt to authenticate the user specified in the form's model
							$scope.signIn = function() {

								// Clear any previous signIn errors
								$scope.authError = null;

								// Try to signIn
								AuthenticationService
										.signIn($scope.user.userName,
												$scope.user.password)
										.then(
												function(response) {
													if (AuthenticationService
															.isAuthenticated()) {
														$scope.user = {};
														$state.go('homepage');
													} else {
														$scope.authError = 'Invalid user name or password combination.';
													}
												},
												function(response) {
													$scope.authError = 'Error communicating with server.';
												});
							};
						} ]);
