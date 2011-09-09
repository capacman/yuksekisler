package com.yuksekisler.interfaces.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.equipment.Brand;

@FacesConverter(forClass = Brand.class)
public class BrandConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return getService(arg0).getBrand(Long.parseLong(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		System.out.println(arg2);
		return ((Brand) arg2).getId().toString();
	}

	private EquipmentService getService(FacesContext fc) {
		return FacesContextUtils.getWebApplicationContext(fc).getBean(
				EquipmentService.class);
	}
}
