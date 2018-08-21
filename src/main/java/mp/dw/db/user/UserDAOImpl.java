package mp.dw.db.user;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class UserDAOImpl extends BaseDAOImpl<UserE, Long> implements UserDAO {
	
	public Iterable<UserE> getAll() {
		return em.createQuery("select u from UserE u").getResultList();
	}
	
	public Iterable<UserE> getUsersWithPerm(String perm, String without) {
		if(perm.equals("canAccept")) {
			Query query = em.createQuery("select u from UserE u where canAccept = true and name != :without");
			query.setParameter("without", without);
			return  query.getResultList();
		} else if(perm.equals("canEdit")) {
			Query query = em.createQuery("select u from UserE u where canEdit = true");
			return  query.getResultList();
		} else {
			return null;
		}
	}

	public UserE getByName(String name) {
		Query query = em.createQuery("select u from UserE u where name = :name");
		query.setParameter("name", name);
		try {
			return (UserE) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
