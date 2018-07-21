package mp.dw.db.doc;

import mp.dw.db.GenericDAO;

public interface DocSumDAO extends GenericDAO<DocSum, Long> {
	public Iterable<DocSum> getByDocId(Long docId);
	public void deleteByDocId(Long docId);
}
