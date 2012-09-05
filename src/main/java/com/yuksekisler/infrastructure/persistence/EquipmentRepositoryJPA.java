package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.work.LifeTime;
import com.yuksekisler.domain.work.WorkDefinition;

@Repository
public class EquipmentRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EquipmentRepository {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentRepositoryJPA.class);

	@Override
	public List<Equipment> findAvailable(final LifeTime lifetime,
			Long categoryID, Long workID) {
		LOGGER.debug("in find available");
		List<Equipment> resultList = new ArrayList<Equipment>();
		List<Equipment> equipments = null;
		if (categoryID == null) {
			equipments = findAll(Equipment.class);
		} else {
			equipments = entityManager
					.createQuery(
							"select e from Equipment e left join fetch e.usedIn where e.category.id=:categoryID and e.erased=false",
							Equipment.class)
					.setParameter("categoryID", categoryID).getResultList();
		}

		for (Equipment available : equipments) {
			if (available.isAvailableFor(lifetime))
				resultList.add(available);
		}
		if (workID != null) {
			WorkDefinition workDefinition = getEntity(workID,
					WorkDefinition.class);
			if (workDefinition.getLifeTime().isActive()) {
				LOGGER.debug("workDefinition is active add all equipments");
				resultList.addAll(workDefinition.getEquipments());
			} else {

				// can cause a lot of memory usage
				LOGGER.debug("equipments starting to fetch");
				List<Equipment> workEquipments = entityManager
						.createQuery(
								"select e from Equipment e left join fetch e.usedIn u where :workDefinition in(u) and u.erased=false",
								Equipment.class)
						.setParameter("workDefinition", workDefinition)
						.getResultList();
				LOGGER.debug("total workEquipment {}", workEquipments.size());
				int i = 0;
				for (Equipment e : workEquipments) {
					boolean conflicted = false;
					for (WorkDefinition w : e.getUsedIn()) {
						if (w.getId() == workID)
							continue;
						if (!w.getErased()
								&& w.getLifeTime().isConflictedWith(lifetime)) {
							conflicted = true;
							i++;
							break;
						}
					}
					if (!conflicted)
						resultList.add(e);
				}
				LOGGER.debug("total workEquipment conflict {}", i);
			}
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
