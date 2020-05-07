package org.chi.dpuk.sensing.platform.user;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User getUser(String userName) {
		Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.userName = :userName");
		query.setParameter("userName", userName);
		return (User) query.uniqueResult();
	}

	@Override
	public User saveUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		return user;
	}

	@Override
	public User getUser(long userId) {
		return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}

	@Override
	public void deleteUser(long userId) {
		User user = this.getUser(userId);
		sessionFactory.getCurrentSession().delete(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersByRole(Role role) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).add(Restrictions.eq("role", role));
		return criteria.list();
	}

}