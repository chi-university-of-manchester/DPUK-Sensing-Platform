package org.chi.dpuk.sensing.platform.study;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.Study;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudyDAOImpl implements StudyDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Study addOrUpdateStudy(Study study) {
		sessionFactory.getCurrentSession().saveOrUpdate(study);
		return study;
	}

	@Override
	public Study getStudy(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Study study where study.name = :name");
		query.setParameter("name", name);
		return (Study) query.uniqueResult();
	}

	@Override
	public Study getStudy(long studyId) {
		return (Study) sessionFactory.getCurrentSession().get(Study.class, studyId);
	}

	@Override
	public void deleteStudy(long studyId) {
		Study study = this.getStudy(studyId);
		sessionFactory.getCurrentSession().delete(study);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Study> getAllStudies() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Study.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
}