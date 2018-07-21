package mp.dw.db.param;

import mp.dw.db.GenericDAO;

public interface ParamDAO extends GenericDAO<Param, Long> {
	public Iterable<Param> getByType(String type);
	public Param getByTypeAndDescr(String type, String descr);
}
