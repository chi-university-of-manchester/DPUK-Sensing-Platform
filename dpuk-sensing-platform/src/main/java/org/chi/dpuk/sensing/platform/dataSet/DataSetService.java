package org.chi.dpuk.sensing.platform.dataSet;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;

public interface DataSetService {

	public DataSet addDataSet(DataSet dataSet);
	
	public DataSet updateDataSet(DataSet dataSet);

	public DataSet getDataSet(long dataSetId);
	
	public void deleteDataSet(long dataSetId);

	public DataSource getDataSourceForDataSet(long dataSetId);

}