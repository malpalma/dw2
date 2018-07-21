package mp.dw.db.doc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class AttachmentDAOImpl extends BaseDAOImpl<Attachment, Long> implements AttachmentDAO {

	public Iterable<Attachment> getByDoc(Document doc) {
		Query query = em.createQuery("select a from Attachment a where document = :doc");
		query.setParameter("doc", doc);
		Iterable<Attachment> resList = query.getResultList(); 
		return resList;
	}
	
}
