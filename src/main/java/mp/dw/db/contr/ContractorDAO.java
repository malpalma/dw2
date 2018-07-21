package mp.dw.db.contr;

import mp.dw.db.GenericDAO;

public interface ContractorDAO extends GenericDAO<Contractor, Long> {
	public Iterable<Contractor> getAll();
}
