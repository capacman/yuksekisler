package com.yuksekisler.application.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.UploadedRepository;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentInActiveUseException;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.work.LifeTime;

public class EquipmentServiceImpl extends AbstractBaseCrudService implements
		EquipmentService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentServiceImpl.class);
	private EquipmentRepository equipmentRepository;
	private UploadedRepository uploadedRepository;

	public void setEquipmentRepository(EquipmentRepository equipmentRepository) {
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public InspectionReport saveInspectionReport(Long equipmentID,
			InspectionReport report, String uploadedUUID) {
		LOGGER.info("equipmentID before find {}", equipmentID);
		LOGGER.info("report before find {}", report);
		Equipment equipment = equipmentRepository.getEntity(equipmentID,
				Equipment.class);
		LOGGER.info("equipment before adding {}", equipment);
		equipment.addInspectionReport(report);
		if (uploadedUUID != null) {
			List<Image> images = uploadedRepository.getByUploadId(uploadedUUID,
					Image.class);
			for (Image image : images) {
				report.addImage(image);
			}
		}
		equipmentRepository.persist(equipment);
		return report;
	}

	public void setUploadedRepository(UploadedRepository uploadedRepository) {
		this.uploadedRepository = uploadedRepository;
	}

	@Override
	public EquipmentRepository getRepository() {
		return equipmentRepository;
	}

	@Override
	public List<Equipment> getAvailableEquipments(LifeTime lifetime,
			Long categoryID) {
		return getRepository().findAvailable(lifetime, categoryID);
	}

	@Override
	public void removeEntity(Long id, Class<Equipment> clazz) {
		Equipment entity = getEntity(id, clazz);
		if (!entity.isInActiveUse()) {
			entity.setErased(true);
			getRepository().merge(entity);
		}
		throw new EquipmentInActiveUseException(entity);
	}

	@Override
	public Equipment findByInspectionReport(InspectionReport inspectionReport) {
		return equipmentRepository.findByInspectionReport(inspectionReport);
	}
}
