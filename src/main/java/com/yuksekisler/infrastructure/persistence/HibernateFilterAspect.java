package com.yuksekisler.infrastructure.persistence;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateFilterAspect {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HibernateFilterAspect.class);
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void enableFilter() {
		HibernateEntityManager hem = entityManager
				.unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		Filter filter = session.enableFilter("erasedFilter");
		filter.setParameter("erasedFilterParam", Boolean.FALSE);
	}
}
