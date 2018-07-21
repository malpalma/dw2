package mp.dw.db.param;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class ParamDAOImpl extends BaseDAOImpl<Param, Long> implements ParamDAO {
	
	public Iterable<Param> getByType(String type) {
		Query query = em.createQuery("select p from Param p where type = :type");
		query.setParameter("type", type);
		return query.getResultList();
	}

	public Param getByTypeAndDescr(String type, String descr) {
		Query query = em.createQuery("select p from Param p where type = :type and description = :descr");
		query.setParameter("type", type);
		query.setParameter("descr", descr);
// generates exception if no result:
//		return (Param) query.getSingleResult();
		List<Param> params = query.getResultList();
		if(params.size() != 1) {
			return null;
		} else {
			return params.get(0);
		}
	}
}
