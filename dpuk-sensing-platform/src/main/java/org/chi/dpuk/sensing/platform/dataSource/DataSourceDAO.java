package org.chi.dpuk.sensing.platform.dataSource;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.DataSource;

public interface DataSourceDAO {

	public DataSource addOrUpdateDataSource(DataSource dataSource);

	public DataSource getDataSource(long dataSourceId);

	public void deleteDataSource(long dataSourceId);

	public List<DataSource> getAllDataSources();

	public DataSource getDataSource(String userName);

	
}