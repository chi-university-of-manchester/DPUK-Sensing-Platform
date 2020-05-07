/**
 * A validation directive to ensure that the user name is unique
 */
angular.module('user').directive('uniqueUserName', [ '$http', function($http) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, ele, attrs, ctrl) {
			scope.$watch(attrs.ngModel, function() {
				if (ctrl.$viewValue) {
					$http({
						method : 'GET',
						url : '/dpuk-sensing-platform/api/user/doesUserExist',
						params : {
							userName : ctrl.$viewValue
						}
					}).success(function(data, status, headers, cfg) {
						ctrl.$setValidity('unique', data === true);
					}).error(function(data, status, headers, cfg) {
						ctrl.$setValidity('unique', true);
					});
				}
			});
		}
	};
} ]);