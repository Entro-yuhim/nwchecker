package com.nwchecker.server.dao;

import com.nwchecker.server.json.ContestPassJson;
import com.nwchecker.server.model.Contest;
import com.nwchecker.server.model.User;
import com.nwchecker.server.utils.ContestStartTimeComparator;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.transaction.TransactionScoped;

@Repository("ContestDAO")
public class ContestDAOImpl extends HibernateDaoSupport implements ContestDAO {

	@Autowired
	public void init(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	@Transactional
	public void addContest(Contest c) {
		getHibernateTemplate().save(c);
	}

	@Override
	@Transactional
	public void updateContest(Contest c) {
		getHibernateTemplate().saveOrUpdate(c);
	}

	@Override
	@Transactional
	public void mergeContest(Contest c) {
		getHibernateTemplate().merge(c);
	}

	@Override
	@Transactional
	public List<Contest> getContests() {
		@SuppressWarnings("unchecked")
		List<Contest> result = (List<Contest>) getHibernateTemplate().find(
				"from Contest");
		return result;
	}

	@Override
	@Transactional
	public Contest getContestByID(int id) {
		@SuppressWarnings("unchecked")
		List<Contest> result = (List<Contest>) getHibernateTemplate().find(
				"from Contest where id=?", id);
		return result.get(0);
	}

	@Override
	@Transactional
	public List<Contest> getContestByStatus(Contest.Status status) {
		return (List<Contest>) getHibernateTemplate().find(
				"from Contest where status=?", status);
	}

	@Override
	@Transactional
	public List<Contest> getContestsWithDynamicRating() {
		return (List<Contest>) getHibernateTemplate().find(
				"from Contest where typeContest.dynamic=?", true);
	}

	@Override
	@Transactional
	public List<Contest> getContestsForRating() {
		return (List<Contest>) getHibernateTemplate()
				.find("from Contest where "
						+ "(status=?  or (typeContest.dynamic=? and status=?)) and hidden=? order by starts desc",
						Contest.Status.ARCHIVE, true, Contest.Status.GOING,
						false);
	}

	@Transactional
	@Override
	public List<Contest> getPagedContests(int pageSize, int startIndex) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session
				.createQuery("from Contest where hidden=:hidden order by starts desc");
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		query.setParameter("hidden", false);
		return query.list();
	}

	@Transactional
	@Override
	public Long getEntryCount() {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session
				.createQuery("select count(*) from Contest where hidden=:hidden");
		query.setParameter("hidden", false);
		return (Long) query.uniqueResult();
	}

	@Transactional
	@Override
	public List<Contest> getPagedContestsByStatus(Contest.Status status,
			int pageSize, int startIndex) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session
				.createQuery("from Contest where status =:status and hidden=:hidden order by starts desc");
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		query.setParameter("hidden", false);
		query.setParameter("status", status);
		return query.list();
	}

	@Transactional
	@Override
	public Long getEntryCount(Contest.Status status) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session
				.createQuery("select count(*) from Contest where status = :status and hidden=:hidden");
		query.setParameter("hidden", false);
		query.setParameter("status", status);
		return (Long) query.uniqueResult();
	}

	@Override
	@Transactional
	public Contest getNearestContest() {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		;
		Query query = session
				.createQuery("from Contest where status='RELEASE' order by starts asc");
		return (Contest) query.setMaxResults(1).uniqueResult();
	}

	@Override
	@Transactional
	public Contest getLastArchivedContest() {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		;
		Query query = session
				.createQuery("from Contest where status='ARCHIVE' order by starts desc");
		return (Contest) query.setMaxResults(1).uniqueResult();
	}

	@Transactional
	@Override
	public List<Contest> getUserPagedContests(int pageSize, int startIndex,
			User user) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("(select * from contest where hidden=0) union distinct (select c.* from contest c inner join contest_users cu on (cu.contest_id=c.id) where cu.user_id=:userId) order by starts desc ");
		query.setFirstResult(startIndex);
		query.setParameter("userId", user.getUserId());
		query.setMaxResults(pageSize);
		// query.setParameter("hidden", true);
		query.addEntity(Contest.class);
		return query.list();
	}

	@Transactional
	@Override
	public Long getUserEntryCount(int userId) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select count(*) as count from ((select * from contest where hidden=0) union distinct (select c.* from contest c inner join contest_users cu on (cu.contest_id=c.id) where cu.user_id=:userId)) x ");
		query.setParameter("userId", userId);
		query.addScalar("count", LongType.INSTANCE);
		return (Long) query.uniqueResult();
	}

	@Transactional
	@Override
	public List<Contest> getUserPagedContestsByStatus(int pageSize,
			int startPage, User user, Contest.Status status) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select * from Contest c inner join contest_users c_u on (c.id = c_u.contest_id) where c_u.user_id =:userId and c.status =:status");
		query.addEntity(Contest.class);
		query.setParameter("userId", user.getUserId());
		query.setParameter("status", status.toString());
		query.setFirstResult(startPage);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Transactional
	@Override
	public Long getUserEntryCountByStatus(int userId, Contest.Status status) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select count(*) as count from Contest c inner join contest_users c_u on (c.id = c_u.contest_id) where c_u.user_id =:userId and c.status =:status");
		query.setParameter("userId", userId);
		query.setParameter("status", status.toString());
		query.addScalar("count", LongType.INSTANCE);
		System.out.println(query.uniqueResult());
		return (Long) query.uniqueResult();
	}
}
