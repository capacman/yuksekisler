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
import com.yuksekisler.domain.equipment.Category;

@RequestMapping("/category")
public class CategoryController extends AbstractBaseController {
	static final Logger LOGGER = LoggerFactory
			.getLogger(CategoryController.class);
	private EquipmentService equipmentService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Category get(@PathVariable("id") Long id) {
		return equipmentService.getCategory(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	Category store(@RequestBody Category category) {
		LOGGER.debug("store category: {}",category);
		return equipmentService.createNewCategory(category);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		equipmentService.removeCategory(equipmentService.getCategory(id));
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Category> query(HttpServletRequest request) {
		return equipmentService.getCategories();
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
