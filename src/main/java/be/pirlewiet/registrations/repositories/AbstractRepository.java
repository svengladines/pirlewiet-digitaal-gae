package be.pirlewiet.registrations.repositories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** De algemene CRUD operaties van elke DAO worden hier beschreven. **/
@Transactional(propagation = Propagation.REQUIRED)
public abstract class AbstractRepository<Entity> {

	@PersistenceContext
	EntityManager em;
	private Class<Entity> entityClass;

	/** Persisteren van een entity met de databank. **/
	public Entity create(Entity entity) {
		try {
			em.persist(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return entity;
	}

	/**
	 * Opvragen van een entity uit de databank op basis van de identifier.
	 **/
	public Entity find(Object key) {
		return em.find(getEntityClass(), key);
	}

	/** Updaten van een entity in de databank. **/
	public Entity update(Entity entity) {
		return em.merge(entity);
	}

	/** Verwijderen van een entity uit de databank. **/
	public void delete(Entity entity) {
		try {
			em.remove(em.merge(entity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ? **/
	public long count() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(getEntityClass())));
		return em.createQuery(cq).getSingleResult().longValue();
	}

	/** Teruggeven van alle entities van een specifiek type uit de databank. **/
	@Transactional
	public List<Entity> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entity> cq = cb.createQuery(getEntityClass());
		cq.select(cq.from(getEntityClass()));
		return em.createQuery(cq).getResultList();
	}

	/** Teruggeven van alle entities van een specifiek type uit de databank die binnen een opgegeven range vallen. **/
	public List<Entity> findRange(int from, int to) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entity> cq = cb.createQuery(getEntityClass());
		cq.select(cq.from(getEntityClass()));
		TypedQuery<Entity> q = em.createQuery(cq);
		q.setMaxResults(to - from);
		q.setFirstResult(from);
		return q.getResultList();
	}

	/** ? **/
	public List<Entity> findRangePager(Integer page, Integer rows, String order, Boolean oplopend) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entity> cq = cb.createQuery(getEntityClass());
		Root<Entity> root = cq.from(getEntityClass());
		if (oplopend) {
			cq.orderBy(cb.asc(root.get(order)));
		} else {
			cq.orderBy(cb.desc(root.get(order)));
		}
		TypedQuery<Entity> q = em.createQuery(cq);

		q.setMaxResults(rows);
		q.setFirstResult((rows * page) - rows);
		return q.getResultList();
	}

	/** Flush uitvoeren om entiteiten in de context te persisten naar de databank. **/
	public void flush() {
		em.flush();
	}

	/** ? **/
	@SuppressWarnings("unchecked")
	private Class<Entity> getEntityClass() {
		if (entityClass == null) {
			Type type = this.getClass().getGenericSuperclass();
			ParameterizedType paramType = (ParameterizedType) type;
			entityClass = (Class<Entity>) paramType.getActualTypeArguments()[0];
		}
		return entityClass;
	}

	/** De versie van de entiteit uit de databank nemen en toepassen op de entiteit in de context. **/
	public void refresh(Entity entity) {
		em.refresh(entity);
	}

}
