package mp.dw.db.contr;

import org.springframework.stereotype.Repository;

import mp.dw.db.BaseDAOImpl;

@Repository
public class ContractorDAOImpl extends BaseDAOImpl<Contractor, Long> implements ContractorDAO {
	public Iterable<Contractor> getAll() {
		return em.createQuery("select c from Contractor c").getResultList();
	}
}
