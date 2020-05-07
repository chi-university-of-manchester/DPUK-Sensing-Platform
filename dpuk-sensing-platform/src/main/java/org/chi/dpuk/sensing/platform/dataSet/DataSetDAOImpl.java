package org.chi.dpuk.sensing.platform.dataSet;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataSetDAOImpl implements DataSetDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSet updateDataSet(DataSet dataSet) {
		sessionFactory.getCurrentSession().saveOrUpdate(dataSet);
		return dataSet;
	}
	
	@Override
	public DataSet getDataSet(long dataSetId) {
		return (DataSet) sessionFactory.getCurrentSession().get(DataSet.class, dataSetId);
	}

	@Override
	public DataSet addDataSet(DataSet dataSet) {
		sessionFactory.getCurrentSession().saveOrUpdate(dataSet);
		return dataSet;
	}

	@Override
	public void deleteDataSet(long dataSetId){
		DataSet dataSet = this.getDataSet(dataSetId);
		sessionFactory.getCurrentSession().delete(dataSet);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataSet> getAllDataSets() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataSet.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

}