/**
 * A directive to set the empty value to null
 */
angular.module('common').directive('emptyToNull', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            ctrl.$parsers.push(function(viewValue) {
                if(viewValue === '') {
                    return null;
                }
                return viewValue;
            });
        }
    };
});