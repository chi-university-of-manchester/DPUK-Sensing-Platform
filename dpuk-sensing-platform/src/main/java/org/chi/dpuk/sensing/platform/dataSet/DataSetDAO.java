package org.chi.dpuk.sensing.platform.dataSet;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.DataSet;

public interface DataSetDAO {

	public DataSet updateDataSet(DataSet dataSet);
	
	public DataSet addDataSet(DataSet dataSet);

	public DataSet getDataSet(long dataSetId);
	
	public void deleteDataSet(long dataSetId);

	public List<DataSet> getAllDataSets();

}