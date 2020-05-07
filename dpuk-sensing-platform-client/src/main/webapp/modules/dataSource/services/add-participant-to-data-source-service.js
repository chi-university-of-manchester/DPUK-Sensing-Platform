angular
		.module('dataSource')
		.factory(
				'AddParticipantToDataSourceService',
				function($resource) {
					return $resource(
							'/dpuk-sensing-platform/api/dataSource/addParticipantToDataSource/:participantId/:dataSourceId',
							{
								participantId : '@participantId',
								dataSourceId : '@dataSourceId'
							});
				})