package mp.dw.db;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDAOImpl<T, ID> implements GenericDAO<T, ID> {
	protected Class<T> entityClass;
	
	@PersistenceContext
	protected EntityManager em;
	
	public BaseDAOImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	public void delete(T entity) {
		em.remove(entity);
	}
	
	public void deleteById(ID id) {
		em.remove(em.getReference(entityClass, id));
	}
	
	public boolean existsById(ID id) {
		return (em.find(entityClass, id) != null);
	}
	
	public T getById(ID id) {
		return (T) em.find(entityClass, id);
	}
	
	public <S extends T> S insert(S entity) {
		em.persist(entity);
		return entity;
	}

	public <S extends T> S update(S entity) {
		em.merge(entity);
		return entity;
	}
}
