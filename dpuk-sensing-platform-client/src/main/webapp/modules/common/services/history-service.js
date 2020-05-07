angular.module('common').factory('HistoryService', function($window) {

	// The public API of the service
	var factory = {
		back: function() {
			// Use timeout here as a workaround for
			// '10 $digest() iterations reached. Aborting!' error
			// when using $window.history.back(); with IE8/9
			setTimeout(function() {
				  $window.history.back();
			}, 10);
		}	
	};
	return factory;
});