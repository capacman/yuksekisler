package com.yuksekisler.infrastructure.persistence;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.yuksekisler.domain.Uploaded;
import com.yuksekisler.domain.UploadedRepository;

public class UploadedRepositoryJPA extends AbstractBaseRepositoryJPA implements
		UploadedRepository {
	public <E extends Uploaded> List<E> getByUploadId(String uploadID,
			Class<E> clazz) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(clazz);
		Root<E> root = query.from(clazz);
		query.select(root).where(
				builder.and(builder.equal(root.get("erased"), Boolean.FALSE),
						builder.equal(root.get("uploadId"), uploadID)));
		return entityManager.createQuery(query).getResultList();
	}

}
