angular
.module('common')
.factory(
		'UtilityService',
		[
		 'ModalService',
		 function(ModalService) {
			 // The public API of the service
			 var factory = {
					 showDeleteConfirmDialog : function(actionText, whatText) {
						 return ModalService
						 .showModal(
								 {
									 templateUrl : 'modules/common/tpl-html/yes-no-modal.tpl.html',
									 controller : 'YesNoDialogController',
									 inputs : {
										 actionText : actionText,//'Delete',
										 whatText : whatText
									 }
								 })
								 .then(
										 function(modal) {
											 modal.element.modal();
											 return modal.close
											 .then(function(
													 result) {
												 return result;
											 });
										 });
					 }
			 };

			 return factory;
		 } ]);