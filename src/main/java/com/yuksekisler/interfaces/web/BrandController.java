package com.yuksekisler.interfaces.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Brand;
@RequestMapping("/brand")
public class BrandController extends AbstractBaseController {
	static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);
	private EquipmentService equipmentService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Brand get(@PathVariable("id") Long id) {
		return equipmentService.getBrand(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	Brand store(@RequestBody Brand brand) {
		LOGGER.debug("store brand: {}",brand);
		return equipmentService.createNewBrand(brand);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		equipmentService.removeBrand(equipmentService.getBrand(id));
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Brand> query(HttpServletRequest request) {
		return equipmentService.getBrands();
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
