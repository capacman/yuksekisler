package com.yuksekisler.web;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.faces.context.FacesContext;
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
		AbstractWorkDefinitionController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1485168065748144028L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkDefinitionFormController.class);
	private Category selectedCategory;
	private EquipmentService equipmentService;
	private List<Equipment> availableEquipments;
	private Boolean isAvailableEquipmentsDrity = true;

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
		refreshAvailableEquipments();
		return availableEquipments;
	}

	protected synchronized void refreshAvailableEquipments() {
		if (isAvailableEquipmentsDrity) {
			availableEquipments = equipmentService.getAvailableEquipments(
					new LifeTime(getStartDate(), getEndDate()),
					getSelectedCategory().getId(), work.getId());
			for (Equipment equipment : work.getEquipments()) {
				availableEquipments.remove(equipment);
			}
			isAvailableEquipmentsDrity = false;
		}
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
		isAvailableEquipmentsDrity = true;
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
		isAvailableEquipmentsDrity = true;
	}

	public void handleDateSelect(DateSelectEvent event) {
		if (event.getComponent().getId().equalsIgnoreCase("startDate")) {
			setStartDate(event.getDate());
		} else {
			setEndDate(event.getDate());
		}
		isAvailableEquipmentsDrity = true;
	}

	public String create() {
		// TODO: there are some checkings
		workService.saveEntity(work);
		return "work";
	}

	public String cancel() {
		return "work";
	}

	@Override
	public void loadWork() {
		// prevents this running from partial rendering
		if (!FacesContext.getCurrentInstance().isPostback())
			super.loadWork();
	}
}
