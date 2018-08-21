package mp.dw.db.doc;

import java.util.List;

import mp.dw.db.GenericDAO;

public interface DocumentDAO extends GenericDAO<Document, Long> {
	public Iterable<Document> getAll();
	public String getStatusById(Long id);
	public String getUserById(Long id);
	public boolean existByContractorData(String name, String address, String regNumber);
	public boolean existByPM(String descr);
}
