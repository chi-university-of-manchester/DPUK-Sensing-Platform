/*
 * The AuthenticationService provides signIn and signOut functions and caches the current signed in user.
 */
angular
		.module('authentication')
		.factory(
				'AuthenticationService',
				[
						'$http',
						'$q',
						function($http, $q) {

							// The public API of the service
							var service = {

								// Cached information about the current user - this will be null if no session is established
								currentUser : null,

								/*
								 * If there is no currentUser object ask the
								 * server to see if a user is already
								 * authenticated. The server will return the
								 * user details if there is an active session.
								 * Returns a promise - hence the use of
								 * $q.when() to wrap the cached
								 * service.currentUser in a promise.
								 */
								requestCurrentUser : function() {
									if (service.isAuthenticated()) {
										// return service.currentUser;
										return $q.when(service.currentUser);
									} else {
										return $http
												.get(
														'/dpuk-sensing-platform/api/authentication/currentUser')
												.then(
														function(response) {
															if (response.data.user) {
																service.currentUser = response.data;
															}
															return service.currentUser;
														});
									}
								},

								// Attempt to authenticate a user by the given userName and password.
								// If successful this will establish a session on the server.
								signIn : function(userName, password) {
									// On signIn clear the cached currentUser
									service.currentUser = null;

									var credentials = {
										userName : userName,
										password : password
									};

									return $http
											.post(
													'/dpuk-sensing-platform/api/authentication/signIn',
													credentials)
											.then(
													function(response) {
														if (response.data.user) {
															service.currentUser = response.data;
														}
														return service.currentUser;
													});
								},

								// signOut the current user on the server - i.e. close the session.
								signOut : function() {
									return $http
											.post(
													'/dpuk-sensing-platform/api/authentication/signOut')
											.then(function(response) {
												service.currentUser = null;
												return response;
											});
								},

								// Is the current user authenticated? i.e. is there a populated service.currentUser object.
								isAuthenticated : function() {
									return !!service.currentUser;
								}
							};

							return service;
						} ]);