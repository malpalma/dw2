package mp.dw.db.doc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class DocItemDAOImpl extends BaseDAOImpl<DocItem, Long> implements DocItemDAO {
	
	public Iterable<DocItem> getByDocId(Long docId) {
		Query query = em.createQuery("select i from DocItem i where document.id = :id");
		query.setParameter("id", docId);
		return query.getResultList();
	}

	public List<Object[]> getGroupedByDocId(Long docId) {
		Query query = em.createQuery("select i.taxDescr, sum(i.price), i.taxRate from DocItem i where document.id = :docId group by i.taxDescr, i.taxRate");
		query.setParameter("docId", docId);
		return query.getResultList();
	}

	public void deleteByDocId(Long docId) {
		List<DocItem> items = (List<DocItem>) this.getByDocId(docId);
		for(DocItem item: items) {
			delete(item);
		}
	}

	public boolean existByTR(String descr, Float value) {
		Query query = em.createQuery("select id from DocItem i where taxDescr = :taxDescr and taxRate = :taxRate");
		query.setParameter("taxDescr", descr);
		query.setParameter("taxRate", value);
		int result = query.getResultList().size();
		if(result > 0)
			return true;
		else
			return false;
	}
	
	public boolean existByUT(String descr) {
		Query query = em.createQuery("select id from DocItem i where unitType = :unitType");
		query.setParameter("unitType", descr);
		int result = query.getResultList().size();
		if(result > 0)
			return true;
		else
			return false;
	}
}
