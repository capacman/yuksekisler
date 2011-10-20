package com.yuksekisler.infrastructure.persistence;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.Uploaded;
import com.yuksekisler.domain.UploadedRepository;

@Repository
public class UploadedRepositoryJPA extends AbstractBaseRepositoryJPA implements
		UploadedRepository {
	public <E extends Uploaded> List<E> getByUploadId(final String uploadID,
			Class<E> clazz) {
		return prepareEntityQuery(clazz, clazz, new InnerQueryBuilder<E, E>() {

			@Override
			public void prepareQuery(CriteriaBuilder qb, CriteriaQuery<E> cq,
					Root<E> root) {
				cq.where(qb.and(cq.getRestriction(),
						qb.equal(root.get("uploadId"), uploadID)));

			}
		}).getResultList();
	}
}
