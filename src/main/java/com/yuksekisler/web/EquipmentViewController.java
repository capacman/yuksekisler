package com.yuksekisler.web;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Equipment;

public class EquipmentViewController extends AbstractEquipmentController {
	private LazyDataModel<Equipment> model;

	public void setEquipmentService(final EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
		model = new LazyDataModel<Equipment>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1961966247543272506L;

			@Override
			public List<Equipment> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				return equipmentService.getEntities(first, pageSize,
						Equipment.class);
			}

		};
		model.setRowCount((int) equipmentService
				.getEntityCount(Equipment.class));
		model.setPageSize(10);
	}

	public LazyDataModel<Equipment> getModel() {
		return model;
	}

	public String addInspectionReport() {
		return "addInspectionReport";
	}
}
