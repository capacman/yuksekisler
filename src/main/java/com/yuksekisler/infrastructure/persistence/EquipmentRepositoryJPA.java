package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.Equipment_;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.work.LifeTime;

@Repository
public class EquipmentRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EquipmentRepository {

	@Override
	public List<Equipment> findAvailable(final LifeTime lifetime) {
		List<Equipment> resultList = new ArrayList<Equipment>();
		for (Equipment available : findAll(Equipment.class)) {
			if (available.isAvailableFor(lifetime))
				resultList.add(available);
		}
		return resultList;
	}

	@Override
	public Equipment findByInspectionReport(
			final InspectionReport inspectionReport) {
		TypedQuery<Equipment> query = entityManager
				.createQuery(
						"select e from Equipment e join e.inspectionReports r where :report in(r)",
						Equipment.class);
		query.setParameter("report", inspectionReport);
		return query.getSingleResult();
	}
}
