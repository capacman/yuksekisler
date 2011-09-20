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
		CriteriaQuery<Long> cq = getBaseEntityQuery(clazz);
		return entityManager.createQuery(cq).getSingleResult();
	}

	public <E> CriteriaQuery<Long> getBaseEntityQuery(Class<E> clazz) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		Root<E> root = cq.from(clazz);
		cq.select(qb.count(root));
		cq.where(qb.equal(root.get("enabled"), Boolean.TRUE));
		return cq;
	}

	@Override
	public <E extends IdEnabledEntity> List<E> findAll(Class<E> clazz) {
		CriteriaQuery<E> query = getAllCriteria(clazz);
		return entityManager.createQuery(query).getResultList();
	}

	public <E> CriteriaQuery<E> getAllCriteria(Class<E> clazz) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(clazz);
		Root<E> root = query.from(clazz);
		return query.select(root).where(
				builder.equal(root.get("enabled"), Boolean.TRUE));
	}

	@Override
	public <E extends IdEnabledEntity> E find(Long id, Class<E> clazz) {
		if (id == null)
			return null;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = qb.createQuery(clazz);
		Root<E> root = cq.from(clazz);
		cq.where(qb.and(qb.equal(root.get("enabled"), Boolean.TRUE),
				qb.equal(root.get("id"), id)));
		return entityManager.createQuery(cq).getSingleResult();
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
		entity.setEnabled(false);
		entityManager.merge(entity);
		entityManager.flush();
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
