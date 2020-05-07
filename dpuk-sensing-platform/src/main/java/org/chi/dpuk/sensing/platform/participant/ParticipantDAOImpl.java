package org.chi.dpuk.sensing.platform.participant;

import java.util.List;

import org.chi.dpuk.sensing.platform.model.Participant;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Participant addOrUpdateParticipant(Participant participant) {
		sessionFactory.getCurrentSession().saveOrUpdate(participant);
		return participant;
	}

	@Override
	public Participant getParticipant(long participantId) {
		return sessionFactory.getCurrentSession().get(Participant.class, participantId);
	}

	@Override
	public void deleteParticipant(long participantId) {
		Participant participant = this.getParticipant(participantId);
		sessionFactory.getCurrentSession().delete(participant);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> getAllParticipants() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Participant.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> getParticipants(String name) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from Participant participant where participant.name = :name");
		query.setParameter("name", name);
		return query.list();
	}

}
