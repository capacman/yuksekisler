package com.yuksekisler.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Category;

@FacesConverter(forClass = Category.class)
public class CategoryConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return getService(arg0).getEntity(Long.parseLong(arg2), Category.class);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ((Category) arg2).getId().toString();
	}

	private EquipmentService getService(FacesContext fc) {
		return FacesContextUtils.getWebApplicationContext(fc).getBean(
				EquipmentService.class);
	}
}
