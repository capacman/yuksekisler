package com.yuksekisler.web;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.DragDropEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.work.LifeTime;

public class WorkDefinitionFormController extends
		AbstractBaseWorkFormController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1485168065748144028L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkDefinitionFormController.class);
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
		List<Equipment> availableEquipments = equipmentService
				.getAvailableEquipments(new LifeTime(getStartDate(),
						getEndDate()), getSelectedCategory().getId(), work
						.getId());
		for (Equipment equipment : work.getEquipments()) {
			availableEquipments.remove(equipment);
		}
		return availableEquipments;
	}

	public EquipmentService getEquipmentService() {
		return equipmentService;
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	public void onEquipmentDrop(DragDropEvent event) {

		Equipment eq = ((Equipment) event.getData());
		LOGGER.info("dropped event id {} ", eq.getId());
		work.addEquipment(eq);
	}

	public void handleClose(ActionEvent event) {
		LOGGER.info("event id {} ",
				event.getComponent().getAttributes().get("equipmentID"));
		Long equipmentID = (Long) event.getComponent().getAttributes()
				.get("equipmentID");
		Iterator<Equipment> iterator = work.getEquipments().iterator();
		while (iterator.hasNext())
			if (iterator.next().getId() == equipmentID) {
				iterator.remove();
				break;
			}
	}

	public void handleDateSelect(DateSelectEvent event) {
		LOGGER.info("event date: {}", event.getDate());
		LOGGER.info("startDate: {}", getStartDate());
		LOGGER.info("endDate: {}", getEndDate());
	}

	public String create() {
		// TODO: there are some checkings
		workService.saveEntity(work);
		return "work";
	}

	public String cancel() {
		return "work";
	}
}
