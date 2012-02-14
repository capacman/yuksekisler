package com.yuksekisler.web;

import java.util.List;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.work.LifeTime;

public class WorkDefinitionFormController extends AbstractBaseWorkFormController {
	private Category selectedCategory;
	private EquipmentService equipmentService;

	public Category getSelectedCategory() {
		if (selectedCategory == null) {
			selectedCategory = workService.getEntities(0, 1, Category.class)
					.get(0);
		}
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<Equipment> getAvailableCategories() {
		return equipmentService.getAvailableEquipments(new LifeTime(getStartDate(),
				getEndDate()), getSelectedCategory().getId(), work.getId());
	}

	public EquipmentService getEquipmentService() {
		return equipmentService;
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
