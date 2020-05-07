package org.chi.dpuk.sensing.platform.dataSet;

import java.util.Date;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataSetServiceImpl implements DataSetService {

	@Autowired
	private DataSetDAO dataSetDAO;

	@Override
	public DataSet addDataSet(DataSet dataSet) {
		dataSet.setPostDate(new Date());
		return dataSetDAO.addDataSet(dataSet);
	}

	@Override
	public DataSet updateDataSet(DataSet dataSet) {
		return dataSetDAO.updateDataSet(dataSet);
	}
	
	@Override
	public DataSet getDataSet(long dataSetId) {
		return dataSetDAO.getDataSet(dataSetId);
	}

	@Override
	public void deleteDataSet(long dataSetId) {
		dataSetDAO.deleteDataSet(dataSetId);
	}
	
	@Override
	public DataSource getDataSourceForDataSet(long dataSetId) {
		return this.getDataSet(dataSetId).getDataSource();
	}

}