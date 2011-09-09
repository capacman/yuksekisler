package com.yuksekisler.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractBaseRepositoryJPA implements BaseRepository {

	protected EntityManager entityManager;

	@Override
	public <E extends IdEnabledEntity> long countEntries(Class<E> clazz) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(clazz)));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@Override
	public <E extends IdEnabledEntity> List<E> findAll(Class<E> clazz) {
		CriteriaQuery<E> query = getAllCriteria(clazz);
		return entityManager.createQuery(query).getResultList();
	}

	public <E> CriteriaQuery<E> getAllCriteria(Class<E> clazz) {
		CriteriaQuery<E> query = entityManager.getCriteriaBuilder()
				.createQuery(clazz);
		Root<E> root = query.from(clazz);
		query.select(root);
		return query;
	}

	@Override
	public <E extends IdEnabledEntity> E find(Long id, Class<E> clazz) {
		if (id == null)
			return null;
		return entityManager.find(clazz, id);
	}

	@Override
	public <E extends IdEnabledEntity> List<E> findEntries(int firstResult,
			int maxResults, Class<E> clazz) {
		return entityManager.createQuery(getAllCriteria(clazz))
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Override
	public <E extends IdEnabledEntity> void remove(E entity) {
		if (this.entityManager.contains(entity)) {
			this.entityManager.remove(entity);
		} else {
			@SuppressWarnings("unchecked")
			E attached = (E) entityManager.find(entity.getClass(),
					entity.getId());
			this.entityManager.remove(attached);
		}
	}

	@Override
	public <E extends IdEnabledEntity> void persist(E employee) {
		this.entityManager.persist(employee);
	}

	@Override
	public void flush() {
		this.entityManager.flush();
	}

	@Override
	public void clear() {
		this.entityManager.clear();
	}

	@Override
	public <E extends IdEnabledEntity> E merge(E entity) {
		E merged = this.entityManager.merge(entity);
		this.entityManager.flush();
		return merged;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
