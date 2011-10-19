package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Bindable.BindableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractBaseRepositoryJPA<ID, E extends IdEnabledEntity<ID>>
		implements BaseRepository<ID, E> {

	protected EntityManager entityManager;

	protected interface InnerQueryBuilder<T, E> {
		void prepareQuery(CriteriaBuilder qb, CriteriaQuery<T> cq, Root<E> root);
	}

	@Override
	public long countEntries(Class<E> clazz) {
		TypedQuery<Long> tq = prepareEntityQuery(Long.class, clazz,
				new InnerQueryBuilder<Long, E>() {

					@Override
					public void prepareQuery(CriteriaBuilder qb,
							CriteriaQuery<Long> cq, Root<E> root) {
						cq.select(qb.count(root));
					}
				});
		return tq.getSingleResult();
	}

	protected <R> TypedQuery<R> prepareEntityQuery(Class<R> resultType,
			Class<E> e, InnerQueryBuilder<R, E> iqb) {

		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<R> cq = qb.createQuery(resultType);
		Root<E> root = cq.from(e);
		cq.where(qb.equal(root.get("erased"), Boolean.FALSE));
		iqb.prepareQuery(qb, cq, root);
		return entityManager.createQuery(cq);
	}

	@Override
	public List<E> findAll(Class<E> clazz) {
		return getAllCriteria(clazz).getResultList();
	}

	protected TypedQuery<E> getAllCriteria(Class<E> clazz) {
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.select(root);
			}
		});
	}

	@Override
	public E getEntity(final ID id, Class<E> clazz) {
		if (id == null)
			return null;
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.where(qb.and(cq.getRestriction(),
						qb.equal(root.get("id"), id)));
			}
		}).getSingleResult();
	}

	@Override
	public List<E> findEntries(int firstResult, int maxResults, Class<E> clazz) {
		return getAllCriteria(clazz).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Override
	public E saveEntity(E e) {
		try {
			return entityManager.merge(e);
		} catch (IllegalArgumentException ex) {
		}
		entityManager.persist(e);
		return e;
	}

	@Override
	public void removeEntity(E entity) {
		entity.setErased(true);
		entityManager.merge(entity);
		entityManager.flush();
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
	public E merge(E entity) {
		E merged = this.entityManager.merge(entity);
		this.entityManager.flush();
		return merged;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<E> query(final QueryParameters parameters, final Class<E> clazz) {

		TypedQuery<E> query = prepareEntityQuery(clazz, clazz,
				new InnerQueryBuilder<E, E>() {

					@Override
					public void prepareQuery(CriteriaBuilder qb,
							CriteriaQuery<E> cq, Root<E> root) {
						Predicate pr = cq.getRestriction();
						if (parameters.getSearchString() != null) {
							Predicate like = qb.like(
									root.get(getSearchAttribute()), "%"
											+ parameters.getSearchString()
											+ "%");
							pr = qb.and(like, pr);
						}
						cq.where(pr);
						if (parameters.hasOrder()) {
							Path<Object> path = root.get(parameters
									.getOrderByField());
							if (parameters.isAscending())
								cq.orderBy(qb.asc(path));
							else
								cq.orderBy(qb.desc(path));
						}

						for (Entry<String, String> queryParameter : parameters
								.getQueryParameters()) {
							try {
								Path<?> parameterPath = foundPath(Arrays
										.asList(queryParameter.getKey().split(
												"\\.")), root);
								cq.where(qb.and(cq.getRestriction(),
										qb.equal(parameterPath, new Object())));
							} catch (IllegalArgumentException e) {
								// could not find path
							}
						}

					}
				});

		if (parameters.hasRange()) {
			query.setFirstResult(parameters.getRangeStart());
			// add plus one since range end inclusive
			query.setMaxResults((parameters.getRangeEnd() - parameters
					.getRangeStart()) + 1);
		}
		return query.getResultList();
	}

	protected Path<?> foundPath(List<String> split, Root<E> root) {
		ArrayDeque<String> pathQueue = new ArrayDeque<String>(split);
		Path<?> path = innerSplit(pathQueue, root);
		if (path.getModel().getBindableType() == BindableType.SINGULAR_ATTRIBUTE)
			return path;
		else
			throw new IllegalArgumentException("");
	}

	private Path<?> innerSplit(ArrayDeque<String> pathQueue, Path<?> root) {
		String attribute = pathQueue.pop();
		Path<Object> childPath = root.get(attribute);
		if (!pathQueue.isEmpty())
			return innerSplit(pathQueue, childPath);
		else {
			return childPath;
		}
	}

	abstract protected SingularAttribute<E, String> getSearchAttribute();

}
