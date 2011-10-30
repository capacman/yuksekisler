package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.work.LifeTime;

@Repository
public class EquipmentRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EquipmentRepository {

	@Override
	public List<Equipment> findAvailable(final LifeTime lifetime,
			Long categoryID) {
		List<Equipment> resultList = new ArrayList<Equipment>();
		List<Equipment> equipments = null;
		if (categoryID == null) {
			equipments = findAll(Equipment.class);
		} else {
			equipments = entityManager
					.createQuery(
							"select e from Equipment e where e.category.id=:categoryID and e.erased=false",
							Equipment.class)
					.setParameter("categoryID", categoryID).getResultList();
		}

		for (Equipment available : equipments) {
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
						"select e from Equipment e join e.inspectionReports r where :report in(r) and e.erased=false",
						Equipment.class);
		query.setParameter("report", inspectionReport);
		return query.getSingleResult();
	}
}
