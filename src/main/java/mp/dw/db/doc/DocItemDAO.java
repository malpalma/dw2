package mp.dw.db.doc;

import java.util.List;

import mp.dw.db.GenericDAO;

public interface DocItemDAO extends GenericDAO<DocItem, Long> {
	public Iterable<DocItem> getByDocId(Long docId);
	public List<Object[]> getGroupedByDocId(Long docId);
	public void deleteByDocId(Long docId);
}
