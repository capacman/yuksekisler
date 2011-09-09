package com.yuksekisler.interfaces.web.jsf;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;

public class EquipmentController {
	private static final Logger LOGGER = org.slf4j.LoggerFactory
			.getLogger(EquipmentController.class);
	private EquipmentService equipmentService;
	private SessionInfo sessionInfo;
	// since equipment value decides whether some views should be visible or not
	// it should be not null
	private Equipment equipment = new Equipment();
	private String newEntityName;
	private String newEntityDescription;
	private String entityType;
	private InspectionReport inspectionReport = new InspectionReport();
	private Long equipmentId;

	private boolean showReportPanel = true;

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	public List<Equipment> getEquipments(int startIndex, int size) {
		return equipmentService.getEquipments(startIndex, size);
	}

	public LazyDataModel<Equipment> getModel() {
		LazyDataModel<Equipment> model = new LazyDataModel<Equipment>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1961966247543272506L;

//			@Override
//			public List<Equipment> load(int arg0, int arg1, String arg2,
//					boolean arg3, Map<String, String> arg4) {
//				return equipmentService.getEquipments(arg0, arg1);
//			}

			@Override
			public List<Equipment> load(int arg0, int arg1, String arg2,
					SortOrder arg3, Map<String, String> arg4) {
				return equipmentService.getEquipments(arg0, arg1);
			}

		};
		model.setRowCount((int) equipmentService.getEquipmentCount());
		model.setPageSize(10);
		return model;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public void setNewEntityName(String value) {
		this.newEntityName = value;
	}

	public String getNewEntityName() {
		return newEntityName;
	}

	public String getNewEntityDescription() {
		return newEntityDescription;
	}

	public void setNewEntityDescription(String newBrandDescription) {
		this.newEntityDescription = newBrandDescription;
	}

	public void newEntityAction() {
		String value = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("entityType");
		if (value.equalsIgnoreCase("brand"))
			equipmentService.createNewBrand(new Brand(newEntityName,
					newEntityDescription));
		else
			equipmentService.createNewCategory(new Category(newEntityName,
					newEntityDescription));
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public InspectionReport getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(InspectionReport inspectionReport) {
		this.inspectionReport = inspectionReport;
	}

	// TODO: may be we should go to view page
	public String addEquipment() {
		LOGGER.debug("addEquipment called with {}", equipment);
		equipmentService.saveEquipment(equipment);
		LOGGER.debug("addEquipment finished");
		return "success";
	}

	public void newInspectionReportAction() {
		String value = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getParameter(
				"reportPanelShow");
		LOGGER.debug("addinspection with value {}", value);

		LOGGER.debug("addInspectionReport called with {}", equipment.getId());
		inspectionReport.setInspector(sessionInfo.getUser());
		LOGGER.debug("inspectionReport {}",inspectionReport);
		equipmentService.saveInspectionReport(equipment.getId(),
				inspectionReport);
		LOGGER.debug("inspectionReport finished");
	}

	// set when view equipment called before rendering phase
	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	// take the given id and load equipment taht will be shown
	public void loadEquipment() {
		if (equipmentId != null) {
			try {
				equipment = equipmentService.getEquipment(equipmentId);
				LOGGER.debug("equipment find {}", equipment);
			} catch (Exception e) {
				equipment = null;
			}
		}
		if (equipment != null){
			LOGGER.debug("equipment find not null");
			LOGGER.debug("equipment has {} inspection reports",equipment.getInspectionReports().size());
		}
	}

	public void beforeListener(PhaseEvent event) {
		LOGGER.info("before phase {}", event.getPhaseId());
	}

	public void eventListener() {

		String value = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("reportPanelShow");
		LOGGER.info("eventListener with value {} and initial {}", value,
				showReportPanel);
		if (value != null && value.equalsIgnoreCase("true"))
			this.showReportPanel = true;
		else
			this.showReportPanel = false;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

}
