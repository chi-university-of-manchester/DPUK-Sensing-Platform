angular
		.module('participant')
		.factory(
				'ParticipantDataSourcesService',
				function($resource) {
					return $resource('/dpuk-sensing-platform/api/participant/dataSource/:participantId');
				})