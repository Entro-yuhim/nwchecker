package com.nwchecker.server.dao;

import com.nwchecker.server.model.ContestPass;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("ContestPassDAO")
public class ContestPassDAOImpl extends HibernateDaoSupport implements ContestPassDAO {

    @Autowired
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    @Transactional
    public void saveContestPass(ContestPass contestPass) {
        getHibernateTemplate().save(contestPass);
    }

    @Override
    @Transactional
    public void updateContestPass(ContestPass contestPass) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.update(contestPass);        		
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public List<ContestPass> getContestPasses(int contestId) {
        return (List<ContestPass>) getHibernateTemplate().find("from ContestPass where contest_id=?", contestId);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
	public List<ContestPass> getValidContestPasses(int contestId) {
    	 return (List<ContestPass>) getHibernateTemplate().find("from ContestPass where contest_id=? and rank > 0", contestId);
	}

    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public ContestPass getContestPassByUserIdAndContestId(int userId, int contestId){
        return ((List<ContestPass>) getHibernateTemplate()
                .find("from ContestPass where contest_id=? and user_userId=?", contestId, userId)).get(0);
    }

	
}
