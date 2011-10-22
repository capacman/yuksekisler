package com.yuksekisler.interfaces.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Category;

@RequestMapping("/category")
public class CategoryController extends
		AbstractBaseCrudController<Long, Category> {
	static final Logger LOGGER = LoggerFactory
			.getLogger(CategoryController.class);
	private EquipmentService equipmentService;

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Override
	protected Class<Category> getEntityClass() {
		return Category.class;
	}

	@Override
	protected CrudService getService() {
		return equipmentService;
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
