package org.chi.dpuk.sensing.platform.dataSource;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.DataSource;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataSourceDAOImpl implements DataSourceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public DataSource getDataSource(long dataSourceId) {
		return (DataSource) sessionFactory.getCurrentSession().get(DataSource.class, dataSourceId);
	}

	@Override
	public DataSource addOrUpdateDataSource(DataSource dataSource) {
		sessionFactory.getCurrentSession().saveOrUpdate(dataSource);
		return dataSource;
	}

	@Override
	public void deleteDataSource(long dataSourceId) {
		DataSource dataSource = this.getDataSource(dataSourceId);
		sessionFactory.getCurrentSession().delete(dataSource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataSource> getAllDataSources() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataSource.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("id"));
		return criteria.list();
	}

	@Override
	public DataSource getDataSource(String userName) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from DataSource dataSource where dataSource.userName = :userName");
		query.setParameter("userName", userName);
		return (DataSource) query.uniqueResult();
	}

	
}