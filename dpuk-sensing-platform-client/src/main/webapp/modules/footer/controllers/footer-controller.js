angular.module('footer').controller('FooterController', ['$scope', '$state', function($scope, $state) {
	$scope.footerCopyright = 'DPUK Sensing Platform, \u00A9 University of Manchester 2015-' + new Date().getFullYear().toString().substr(2,2);	
}]);