/**
 * A validation directive to ensure that name of the study is unique
 */
angular.module('study').directive('uniqueName', ['$http', function($http) {
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, ele, attrs, ctrl) {
			scope.$watch(attrs.ngModel, function() {
				if (ctrl.$viewValue) {
					$http({
						method: 'GET',
						url: '/dpuk-sensing-platform/api/study/doesStudyExist',
						params: {name: ctrl.$viewValue}
					}).success(function(data, status, headers, cfg) {
						ctrl.$setValidity('unique', data === true);
			        }).error(function(data, status, headers, cfg) {
			        	ctrl.$setValidity('unique', true);
			        });
				}
			});
		}
	};	
}]);