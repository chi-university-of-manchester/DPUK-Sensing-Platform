angular.module('authentication').config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('interceptor');
}]);