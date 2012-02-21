package com.yuksekisler.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;

public abstract class AbstractEquipmentController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractEquipmentController.class);
	protected EquipmentService equipmentService;
	protected Equipment equipment = new Equipment();
	protected Long equipmentId;

	public AbstractEquipmentController() {
		super();
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public List<Category> getCategories() {
		return equipmentService.getAllEntities(Category.class);
	}

	public List<Brand> getBrands() {
		return equipmentService.getAllEntities(Brand.class);
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public void loadEquipment() {
		if (equipmentId != null) {
			LOGGER.info("equipment id is {}", equipmentId);
			try {
				equipment = equipmentService.getEntity(equipmentId,
						Equipment.class);
			} catch (Exception e) {
				equipment = null;
			}
		} else {
			LOGGER.info("equipment id is null");
		}
	}
}