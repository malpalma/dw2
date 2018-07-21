package mp.dw.db;

public interface GenericDAO<T, ID> {
	void delete(T entity);
	void deleteById(ID id);
	boolean existsById(ID id);
	T getById(ID id);
	<S extends T> S insert(S entity);
	<S extends T> S update(S entity);
}
