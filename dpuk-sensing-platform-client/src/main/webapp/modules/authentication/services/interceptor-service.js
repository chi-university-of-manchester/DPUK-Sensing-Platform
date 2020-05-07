/* HTTP interceptor to redirect HTTP 401 responses to the signIn page. */
angular.module('authentication').factory('interceptor', ['$q', '$injector', 
    function($q, $injector) {	
	return {
		'response': function(response) {
			return response;
		},

	   'responseError': function(rejection) {
		   if (
			   (rejection.status === 401) ||	// Unauthorized
			   (rejection.status === 403)		// Forbidden
		   ) {
			   // We can't inject the AuthenticationService directly into the http interceptor
			   // because of a circular dependency - i.e. AuthenticationService depends on $http
			   // so we look it up using the $injector and cache it.
			   var AuthenticationService = $injector.get('AuthenticationService');
			   var state = $injector.get('$state');
			   AuthenticationService.signOut();
			   state.go('app.signIn');
		   }
		   // default behaviour
		   return $q.reject(rejection);
	   }
	};
	
}]);