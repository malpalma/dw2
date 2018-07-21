package mp.dw.db.doc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class DocStageDAOImpl extends BaseDAOImpl<DocStage, Long> implements DocStageDAO {
	public Iterable<DocStage> getByDocId(Long docId) {
		Query query = em.createQuery("select s from DocStage s where document.id = :id order by date");
		query.setParameter("id", docId);
		return query.getResultList();
	}
	
	public void deleteByDocId(Long docId) {
		List<DocStage> stages = (List<DocStage>) this.getByDocId(docId);
		for(DocStage stage: stages) {
			delete(stage);
		}
	}
}
