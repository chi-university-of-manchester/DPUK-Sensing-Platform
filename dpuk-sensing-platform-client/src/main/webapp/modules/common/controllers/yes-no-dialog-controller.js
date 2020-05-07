angular.module('common').controller(
		'YesNoDialogController',
		[ '$scope', 'close', 'actionText', 'whatText',
		  function($scope, close, actionText, whatText) {
			$scope.actionText = actionText;
			$scope.whatText = whatText;
			$scope.close = function(result) {
				close(result, 500); // close, but give 500ms for bootstrap to animate
			};
		} ]);