package mp.dw.db.doc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class DocSumDAOImpl extends BaseDAOImpl<DocSum, Long> implements DocSumDAO {
	
	public Iterable<DocSum> getByDocId(Long docId) {
		Query query = em.createQuery("select s from DocSum s where document.id = :id");
		query.setParameter("id", docId);
		return query.getResultList();
	}

	public void deleteByDocId(Long docId) {
		List<DocSum> sums = (List<DocSum>) this.getByDocId(docId);
		for(DocSum sum: sums) {
			delete(sum);
		}
	}
}
