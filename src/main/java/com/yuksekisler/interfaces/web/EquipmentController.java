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
import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.equipment.Equipment;

@RequestMapping("/equipment")
public class EquipmentController extends AbstractBaseController {
	static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentController.class);
	private EquipmentService equipmentService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Equipment get(@PathVariable("id") Long id) {
		return equipmentService.getEquipment(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	Equipment store(@RequestBody Equipment equipment) {
		return equipmentService.saveEquipment(equipment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		equipmentService.removeEquipment(equipmentService.getEquipment(id));
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Equipment> query(HttpServletRequest request) {
		QueryParameters parameters = prepareQueryParameters(request);
		if (parameters.getSearchString() != null
				&& !parameters.getSearchString().isEmpty()) {
			return equipmentService.queryEquipment(parameters);
		} else {
			return equipmentService.getEquipments();
		}
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
