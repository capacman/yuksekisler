package com.yuksekisler.interfaces.web;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.equipment.InspectionStatus;

@RequestMapping("/equipment")
public class EquipmentController {
	static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentController.class);
	private EquipmentService equipmentService;
	private FileService fileService;
	private AbstractBaseCrudController<Long, Equipment> equipmentDelagateController;

	@InitBinder({ "stockEntrance", "bestBeforeDate", "productionDate",
			"inspectionDate" })
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					LOGGER.debug("starting to parse value: {}", value);
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					LOGGER.warn("web data binder failed for value: {}", value);
					setValue(null);
				}
			}

			public String getAsText() {
				return new SimpleDateFormat("dd/MM/yyyy")
						.format((Date) getValue());
			}

		});

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Equipment get(@PathVariable("id") Long id) {
		return this.equipmentDelagateController.get(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody
	Equipment store(
			@RequestParam("productName") String productName,
			@RequestParam("productCode") String productCode,
			@RequestParam("category") Long categoryId,
			@RequestParam("brand") Long brandId,
			@RequestParam("stockEntrance") Date stockEntrance,
			@RequestParam("bestBeforeDate") Date bestBeforeDate,
			@RequestParam("productionDate") Date productionDate,
			@RequestParam(value = "filesUUID", required = false) String filesUUID) {
		Equipment equipment = new Equipment();
		saveOrUpdateEquipment(productName, productCode, categoryId, brandId,
				stockEntrance, bestBeforeDate, productionDate, filesUUID,
				equipment);
		equipment = equipmentService.saveEntity(equipment);
		return equipment;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public @ResponseBody
	Equipment update(
			@PathVariable("id") Long equipmentId,
			@RequestParam("productName") String productName,
			@RequestParam("productCode") String productCode,
			@RequestParam("category") Long categoryId,
			@RequestParam("brand") Long brandId,
			@RequestParam("stockEntrance") Date stockEntrance,
			@RequestParam("bestBeforeDate") Date bestBeforeDate,
			@RequestParam("productionDate") Date productionDate,
			@RequestParam(value = "filesUUID", required = false) String filesUUID) {
		Equipment equipment = equipmentService.getEntity(equipmentId,
				Equipment.class);
		saveOrUpdateEquipment(productName, productCode, categoryId, brandId,
				stockEntrance, bestBeforeDate, productionDate, filesUUID,
				equipment);
		equipment = equipmentService.saveEntity(equipment);
		return equipment;
	}

	protected void saveOrUpdateEquipment(String productName,
			String productCode, Long categoryId, Long brandId,
			Date stockEntrance, Date bestBeforeDate, Date productionDate,
			String filesUUID, Equipment equipment) {
		equipment.setProductCode(productCode);
		equipment.setProductName(productName);
		equipment.setProductionDate(productionDate);
		equipment.setStockEntrance(stockEntrance);
		equipment.setBestBeforeDate(bestBeforeDate);
		equipment.setCategory(equipmentService.getEntity(categoryId,
				Category.class));
		equipment.setBrand(equipmentService.getEntity(brandId, Brand.class));
		if (filesUUID != null) {
			LOGGER.debug(
					"some associated files has been found for equipment with id: {}",
					filesUUID);
			List<Image> images = fileService.getFiles(filesUUID, Image.class);
			LOGGER.debug("{} number of files has been found", images.size());
			for (Image image : images) {
				equipment.addImage(image);
			}
		}
		LOGGER.debug("store equipment {}", equipment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		this.equipmentDelagateController.delete(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Equipment>> query(HttpServletRequest request,
			HttpServletResponse response) {
		return this.equipmentDelagateController.query(request);
	}

	@RequestMapping(value = "/{id}/inspectionReport", method = RequestMethod.POST)
	public @ResponseBody
	InspectionReport storeInspectionReport(
			@PathVariable("id") Long equipmentId,
			@RequestParam("stateSelect") InspectionStatus inspectionStatus,
			@RequestParam("report") String report,
			@RequestParam("inspectionDate") Date inspectionDate,
			@RequestParam(value = "filesUUID", required = false) String filesUUID) {
		// Equipment equipment = equipmentService.getEquipment(equipmentId);
		InspectionReport inspectionReport = new InspectionReport();
		inspectionReport.setInspectionDate(inspectionDate);
		inspectionReport.setInspector((Employee) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal());
		inspectionReport.setReport(report);
		inspectionReport.setStatus(inspectionStatus);
		inspectionReport = equipmentService.saveInspectionReport(equipmentId,
				inspectionReport, filesUUID);
		return inspectionReport;
	}

	public void setEquipmentService(final EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
		this.equipmentDelagateController = new AbstractBaseCrudController<Long, Equipment>() {

			@Override
			protected Class<Equipment> getEntityClass() {
				return Equipment.class;
			}

			@Override
			protected CrudService getService() {
				return equipmentService;
			}

			@Override
			protected String getSearchAttribute() {
				return "productName";
			}
		};
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
}
