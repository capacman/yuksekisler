package com.yuksekisler.interfaces.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Equipment;

@FacesConverter(forClass = Equipment.class, value = "com.yuksekisler.interfaces.web.jsf.converter.EquipmentConverter")
public class EquipmentConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 != null && !arg2.isEmpty())
			return getService(arg0).getEquipment(Long.parseLong(arg2));
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 != null)
			return ((Equipment) arg2).getId().toString();
		return null;
	}

	private EquipmentService getService(FacesContext fc) {
		return FacesContextUtils.getWebApplicationContext(fc).getBean(
				EquipmentService.class);
	}

}
