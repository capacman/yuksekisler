package com.yuksekisler.interfaces.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Brand;

@RequestMapping("/brand")
public class BrandController extends AbstractBaseCrudController<Long, Brand> {
	static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);
	private EquipmentService equipmentService;

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Override
	protected Class<Brand> getEntityClass() {
		return Brand.class;
	}

	@Override
	protected CrudService getService() {
		return equipmentService;
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}
}
