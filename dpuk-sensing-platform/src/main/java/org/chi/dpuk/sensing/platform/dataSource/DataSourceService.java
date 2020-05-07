package org.chi.dpuk.sensing.platform.dataSource;

import java.util.List;
import java.util.Set;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;

public interface DataSourceService {

	public DataSource addOrUpdateDataSource(DataSource dataSource);

	public DataSource getDataSource(long dataSourceId);

	public void deleteDataSource(long dataSourceId);

	public List<DataSource> getAllDataSources();

	public Participant getParticipantForDataSource(long dataSourceId);

	public DataSource addParticipantToDataSource(long participantId, long dataSourceId);

	public Set<DataSet> getDataSetsForDataSource(long dataSourceId);

	public DataSource addDataSetToDataSource(DataSet dataSet, long dataSourceId);

	public boolean doesDataSourceExist(String userName);

	public DataSource getDataSource(String userName);

	
}