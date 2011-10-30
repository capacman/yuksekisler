package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Bindable.BindableType;

import org.springframework.core.convert.ConversionService;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.HasName;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractBaseRepositoryJPA implements BaseRepository {
	protected EntityManager entityManager;
	protected ConversionService conversionService;

	protected interface InnerQueryBuilder<T, E> {
		void prepareQuery(CriteriaBuilder qb, CriteriaQuery<T> cq, Root<E> root);
	}

	@Override
	public <E extends IdEnabledEntity<?>> long countEntries(Class<E> clazz) {
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

	protected <R, E> TypedQuery<R> prepareEntityQuery(Class<R> resultType,
			Class<E> e, InnerQueryBuilder<R, E> iqb) {

		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<R> cq = qb.createQuery(resultType);
		Root<E> root = cq.from(e);
		cq.where(qb.equal(root.get("erased"), Boolean.FALSE));
		iqb.prepareQuery(qb, cq, root);
		return entityManager.createQuery(cq);
	}

	@Override
	public <E extends IdEnabledEntity<?>> List<E> findAll(Class<E> clazz) {
		return getAllCriteria(clazz).getResultList();
	}

	protected <E> TypedQuery<E> getAllCriteria(Class<E> clazz) {
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.select(root);
			}
		});
	}

	@Override
	public <ID, E extends IdEnabledEntity<ID>> E getEntity(final ID id,
			Class<E> clazz) {
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
	public <E extends IdEnabledEntity<?>> List<E> findEntries(int firstResult,
			int maxResults, Class<E> clazz) {
		return getAllCriteria(clazz).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Override
	public <E extends IdEnabledEntity<?>> E saveEntity(E e) {
		try {
			return entityManager.merge(e);
		} catch (IllegalArgumentException ex) {
		}
		entityManager.persist(e);
		return e;
	}

	@Override
	public <E extends IdEnabledEntity<?>> void removeEntity(E entity) {
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
	public <T> T merge(T entity) {
		T merged = this.entityManager.merge(entity);
		this.entityManager.flush();
		return merged;
	}

	@Override
	public <T> T persist(T entity) {
		this.entityManager.persist(entity);
		this.entityManager.flush();
		return entity;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public <E extends IdEnabledEntity<?>> List<E> query(
			final QueryParameters parameters, Class<E> clazz,
			final String searchAttribute) {

		TypedQuery<E> query = prepareEntityQuery(clazz, clazz,
				new InnerQueryBuilder<E, E>() {

					@Override
					public void prepareQuery(CriteriaBuilder qb,
							CriteriaQuery<E> cq, Root<E> root) {
						Predicate pr = cq.getRestriction();
						if (parameters.getSearchString() != null
								&& searchAttribute != null) {
							Predicate like = qb.like(
									getSearchAttribute(root, searchAttribute),
									"%" + parameters.getSearchString() + "%");
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

						for (Entry<String, Object> queryParameter : parameters
								.getQueryParameters()) {
							if ("*".equalsIgnoreCase((String) queryParameter
									.getValue()))
								continue;
							try {
								Path<?> parameterPath = foundPath(
										queryParameter.getKey(), root);
								if (((String) queryParameter.getValue())
										.contains("*")) {
									searchForLike(qb, cq, queryParameter,
											parameterPath);

								} else {
									searchForEqual(qb, cq, queryParameter,
											parameterPath);
								}
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

	protected <E extends IdEnabledEntity<?>> Path<?> foundPath(
			String queryAttribute, Root<E> root) {
		String[] queryAttributePath = queryAttribute.split("\\.");
		ArrayDeque<String> pathQueue = new ArrayDeque<String>(
				queryAttributePath.length);
		for (String pathPart : queryAttributePath) {
			pathQueue.add(pathPart);
		}

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

	@SuppressWarnings("unchecked")
	protected <E extends IdEnabledEntity<?>> Expression<String> getSearchAttribute(
			Root<E> root, String searchAttribute) {
		Path<?> foundPath = foundPath(searchAttribute, root);
		if (foundPath.getModel().getBindableJavaType().equals(String.class))
			return (Expression<String>) foundPath;
		throw new IllegalArgumentException("searchAttribute: "
				+ searchAttribute
				+ " is not string but "
				+ foundPath.getModel().getBindableJavaType().getClass()
						.getCanonicalName());
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public <E extends HasName<?>> List<E> findByName(final String name,
			Class<E> clazz) {
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.where(qb.and(cq.getRestriction(),
						qb.equal(root.get("name"), name)));
			}
		}).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <E> void searchForLike(CriteriaBuilder qb, CriteriaQuery<E> cq,
			Entry<String, Object> queryParameter, Path<?> parameterPath) {
		if (!parameterPath.getModel().getBindableJavaType()
				.equals(String.class))
			throw new IllegalArgumentException(queryParameter.getKey()
					+ " needs to be type string to be searchable");

		String likeSentence = ((String) queryParameter.getValue())
				.replaceFirst("\\*", "%");
		likeSentence = likeSentence.replace("\\*", "");
		cq.where(qb.and(cq.getRestriction(),
				qb.like((Path<String>) parameterPath, likeSentence)));
	}

	private <E> void searchForEqual(CriteriaBuilder qb, CriteriaQuery<E> cq,
			Entry<String, Object> queryParameter, Path<?> parameterPath) {
		cq.where(qb.and(cq.getRestriction(), qb.equal(parameterPath,
				conversionService.convert(queryParameter.getValue(),
						parameterPath.getModel().getBindableJavaType()))));
	}

	@Override
	public <ID, E extends IdEnabledEntity<ID>> List<E> getEntities(
			final List<ID> idx, Class<E> clazz) {
		if (idx.isEmpty())
			return new ArrayList<E>();
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.where(qb.and(cq.getRestriction(), root.get("id").in(idx)));
			}
		}).getResultList();
	}
}
