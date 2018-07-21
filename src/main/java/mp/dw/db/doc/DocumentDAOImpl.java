package mp.dw.db.doc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class DocumentDAOImpl extends BaseDAOImpl<Document, Long> implements DocumentDAO {

	public Iterable<Document> getAll() {
		return em.createQuery("select d from Document d").getResultList();
	}

	public String getStatusById(Long id) {
		Query query = em.createQuery("select status from Document d where id = :id");
		query.setParameter("id", id);
		return (String) query.getResultList().get(0);
	}
	
	public String getUserById(Long id) {
		Query query = em.createQuery("select user from Document d where id = :id");
		query.setParameter("id", id);
		List res = query.getResultList();
		if(res.get(0) == null) {
			return "";
		} else {
			return (String) res.get(0);
		}
	}
	
}
