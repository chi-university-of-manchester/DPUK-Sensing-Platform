'use strict';
/*
	This file contains all the application modules and their dependencies.
	We use separate modules for each feature of the application.
*/

angular.module('app', ['ui.router', 'angularModalService', 'ui.bootstrap', 'authentication', 'common', 'menu', 'footer', 'study', 'participant', 'user', 'dataSource', 'dataSet']);
angular.module('authentication', ['ui.router']);
angular.module('common', []);
angular.module('menu', ['ui.router']);
angular.module('footer', []);
angular.module('study', ['ngResource', 'ui.router']);
angular.module('participant', ['ngResource', 'ui.router']);
angular.module('user', ['ngResource', 'ui.router']);
angular.module('dataSource', ['ngResource', 'ui.router', 'ui.bootstrap']);
angular.module('dataSet', ['ngResource', 'ui.router']);