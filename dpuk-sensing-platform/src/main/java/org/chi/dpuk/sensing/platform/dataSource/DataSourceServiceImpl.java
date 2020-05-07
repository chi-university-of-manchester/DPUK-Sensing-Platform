package org.chi.dpuk.sensing.platform.dataSource;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.participant.ParticipantDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataSourceServiceImpl implements DataSourceService {

	@Autowired
	private DataSourceDAO dataSourceDAO;

	@Autowired
	private ParticipantDAO participantDAO;

	@Override
	public DataSource addOrUpdateDataSource(DataSource dataSource) {
		return dataSourceDAO.addOrUpdateDataSource(dataSource);
	}

	@Override
	public DataSource getDataSource(long dataSourceId) {
		return dataSourceDAO.getDataSource(dataSourceId);
	}

	@Override
	public List<DataSource> getAllDataSources() {
		return dataSourceDAO.getAllDataSources();
	}

	@Override
	public void deleteDataSource(long dataSourceId) {
		dataSourceDAO.deleteDataSource(dataSourceId);
	}

	@Override
	public Participant getParticipantForDataSource(long dataSourceId) {
		return this.getDataSource(dataSourceId).getParticipant();
	}

	@Override
	public DataSource addParticipantToDataSource(long participantId, long dataSourceId) {
		Participant participant = participantDAO.getParticipant(participantId);
		DataSource dataSource = this.getDataSource(dataSourceId);
		dataSource.setParticipant(participant);
		return this.addOrUpdateDataSource(dataSource);
	}

	@Override
	public Set<DataSet> getDataSetsForDataSource(long dataSourceId) {
		return this.getDataSource(dataSourceId).getDataSets();
	}

	@Override
	public DataSource addDataSetToDataSource(DataSet dataSet, long dataSourceId) {
		DataSource dataSource = this.getDataSource(dataSourceId);
		dataSet.setPostDate(new Date());
		dataSource.addDataSet(dataSet);
		dataSet.setDataSource(dataSource);
		return this.addOrUpdateDataSource(dataSource);
	}

	@Override
	public boolean doesDataSourceExist(String userName) {
		return this.getDataSource(userName) != null ? false : true;
	}

	@Override
	public DataSource getDataSource(String userName) {
		return dataSourceDAO.getDataSource(userName);
	}

	
}