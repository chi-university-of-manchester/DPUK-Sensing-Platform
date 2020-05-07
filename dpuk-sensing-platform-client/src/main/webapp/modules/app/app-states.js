'use strict';
/* configure all states */

angular
		.module('app')
		.config(
				[
						'$stateProvider',
						'$urlRouterProvider',
						function($stateProvider, $urlRouterProvider) {

							$urlRouterProvider.otherwise('/homepage');

							$stateProvider
									.state(
											'homepage',
											{
												url : '/homepage',
												templateUrl : 'modules/app/tpl-html/homepage.tpl.html',
												controller : 'HomepageController'
											})
									.state(
											'app',
											{
												url : '/',
												templateUrl : 'modules/app/tpl-html/app.tpl.html'
											})
									.state(
											'app.signIn',
											{
												url : 'signIn',
												views : {
													'content' : {
														templateUrl : 'modules/authentication/tpl-html/authentication-form.tpl.html',
														controller : 'AuthenticationController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.users',
											{
												url : 'users',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/user/tpl-html/users-view.tpl.html',
														controller : 'UsersViewController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.userAdd',
											{
												url : 'user/add',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/user/tpl-html/user-add.tpl.html',
														controller : 'UserAddController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.userEdit',
											{
												url : 'user/edit/:userId',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/user/tpl-html/user-edit.tpl.html',
														controller : 'UserEditController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.userStudies',
											{
												url : 'user/studies/:userId',
												params : {
													userName : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/user/tpl-html/user-studies.tpl.html',
														controller : 'UserStudiesController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.studyUsers',
											{
												url : 'study/users/:studyId',
												params : {
													studyName : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/study/tpl-html/study-users.tpl.html',
														controller : 'StudyUsersController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.studyParticipants',
											{
												url : 'study/participants/:studyId',
												params : {
													studyName : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/study/tpl-html/study-participants.tpl.html',
														controller : 'StudyParticipantsController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.studyAdd',
											{
												url : 'study/add',
												params : {
													userId : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/study/tpl-html/study.tpl.html',
														controller : 'StudyAddController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.studyEdit',
											{
												url : 'study/edit/:studyId',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/study/tpl-html/study.tpl.html',
														controller : 'StudyEditController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.participantDataSources',
											{
												url : 'participant/dataSources/:participantId',
												params : {
													participantName : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/participant/tpl-html/participant-data-sources.tpl.html',
														controller : 'ParticipantDataSourcesController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.participantAdd',
											{
												url : 'participant/add',
												params : {
													studyId : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/participant/tpl-html/participant.tpl.html',
														controller : 'ParticipantAddController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.participantEdit',
											{
												url : 'participant/edit/:participantId',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/participant/tpl-html/participant.tpl.html',
														controller : 'ParticipantEditController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.dataSourceAdd',
											{
												url : 'dataSource/add',
												params : {
													participantId : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/dataSource/tpl-html/data-source.tpl.html',
														controller : 'DataSourceAddController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.dataSourceEdit',
											{
												url : 'dataSource/edit/:dataSourceId',
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/dataSource/tpl-html/data-source.tpl.html',
														controller : 'DataSourceEditController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.dataSourceDataSets',
											{
												url : 'dataSource/dataSets/:dataSourceId',
												params : {
													dataSourceName : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/dataSource/tpl-html/data-source-data-sets.tpl.html',
														controller : 'DataSourceDataSetsController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})
									.state(
											'app.addDataSetToDataSource',
											{
												url : 'dataSource/addDataSetToDataSource',
												params : {
													dataSourceId : null
												},
												views : {
													'menu' : {
														templateUrl : 'modules/menu/tpl-html/menu.tpl.html',
														controller : 'MenuController'
													},
													'content' : {
														templateUrl : 'modules/dataSource/tpl-html/add-data-set-to-data-source.tpl.html',
														controller : 'AddDataSetToDataSourceController'
													},
													'footer' : {
														templateUrl : 'modules/footer/tpl-html/footer.tpl.html',
														controller : 'FooterController'
													}
												}
											})

						} ]);