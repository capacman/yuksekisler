package com.yuksekisler.infrastructure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.Equipment_;

@Repository
public class EquipmentRepositoryJPA extends
		AbstractBaseRepositoryJPA<Long, Equipment> implements
		EquipmentRepository {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentRepositoryJPA.class);

	@Override
	public List<Equipment> queryEquipment(QueryParameters queryParameters) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Equipment> c = qb.createQuery(Equipment.class);
		Root<Equipment> root = c.from(Equipment.class);
		Predicate pr = qb.equal(root.get(Equipment_.erased), Boolean.FALSE);

		if (queryParameters.getSearchString() != null) {
			Predicate like = qb.like(root.get(Equipment_.productName), "%"
					+ queryParameters.getSearchString() + "%");
			pr = qb.and(like, pr);
		}
		c.where(pr);
		if (queryParameters.hasOrder()) {
			Path<Object> path = root.get(queryParameters.getOrderByField());
			if (queryParameters.isAscending())
				c.orderBy(qb.asc(path));
			else
				c.orderBy(qb.desc(path));
		}

		TypedQuery<Equipment> query = entityManager.createQuery(c);
		if (queryParameters.hasRange()) {
			query.setFirstResult(queryParameters.getRangeStart());
			// add plus one since range end inclusive
			query.setMaxResults((queryParameters.getRangeEnd() - queryParameters
					.getRangeStart()) + 1);
		}
		return query.getResultList();
	}
}
