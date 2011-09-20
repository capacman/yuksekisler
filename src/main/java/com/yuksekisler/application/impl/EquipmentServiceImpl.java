package com.yuksekisler.application.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.InspectionReport;

public class EquipmentServiceImpl implements EquipmentService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentServiceImpl.class);
	private EquipmentRepository equipmentRepository;

	public void setEquipmentRepository(EquipmentRepository equipmentRepository) {
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public Equipment createEquipment(Category category, Brand brand,
			Date stockEntranceDate, Date bestBefore, Date productionDate,
			String productCode, String productName) {
		Equipment equipment = new Equipment();
		equipment.setCategory(category);
		equipment.setBrand(brand);
		equipment.setStockEntrance(stockEntranceDate);
		equipment.setBestBeforeDate(bestBefore);
		equipment.setProductionDate(productionDate);
		equipment.setProductCode(productCode);
		equipment.setProductName(productName);
		equipmentRepository.persist(equipment);
		return equipment;
	}

	@Override
	public Equipment saveEquipment(Equipment equipment) {
		try {
			return equipmentRepository.merge(equipment);
		} catch (IllegalArgumentException e) {
			// not an entity
		}
		equipmentRepository.persist(equipment);
		return equipment;
	}

	@Override
	public List<Equipment> getEquipments(int startIndex, int size) {
		return equipmentRepository.findEntries(startIndex, size,
				Equipment.class);
	}

	@Override
	public long getEquipmentCount() {
		return equipmentRepository.countEntries(Equipment.class);
	}

	@Override
	public Brand createNewBrand(Brand brand) {
		equipmentRepository.persist(brand);
		return brand;
	}

	@Override
	public Category createNewCategory(Category category) {
		equipmentRepository.persist(category);
		return category;
	}

	@Override
	public List<Brand> getBrands() {
		return equipmentRepository.findAll(Brand.class);
	}

	@Override
	public List<Category> getCategories() {
		return equipmentRepository.findAll(Category.class);
	}

	@Override
	public List<Equipment> getEquipments() {
		return equipmentRepository.findAll(Equipment.class);
	}

	@Override
	public Brand getBrand(Long id) {
		return equipmentRepository.find(id, Brand.class);
	}

	@Override
	public Category getCategory(Long id) {
		return equipmentRepository.find(id, Category.class);
	}

	@Override
	public Equipment getEquipment(Long id) {
		return equipmentRepository.find(id, Equipment.class);
	}

	@Override
	public InspectionReport saveInspectionReport(Long equipmentID,
			InspectionReport report) {
		LOGGER.info("equipmentID before find {}", equipmentID);
		LOGGER.info("report before find {}", report);
		Equipment equipment = equipmentRepository.find(equipmentID,
				Equipment.class);
		LOGGER.info("equipment before adding {}", equipment);
		equipment.addInspectionReport(report);
		equipmentRepository.persist(equipment);
		return report;
	}

	@Override
	public void removeEquipment(Equipment equipment) {
		equipmentRepository.remove(equipment);
	}

	@Override
	public List<Equipment> queryEquipment(QueryParameters queryParameters) {
		return equipmentRepository.queryEquipment(queryParameters);
	}

	@Override
	public void removeCategory(Category category) {
		equipmentRepository.remove(category);
	}

	@Override
	public void removeBrand(Brand brand) {
		equipmentRepository.remove(brand);
	}

}
