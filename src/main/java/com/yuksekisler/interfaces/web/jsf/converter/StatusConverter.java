package com.yuksekisler.interfaces.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.yuksekisler.domain.equipment.InspectionStatus;

@FacesConverter(forClass = com.yuksekisler.domain.equipment.InspectionStatus.class, value = "com.yuksekisler.domain.equipment.StatusConverter")
public class StatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return InspectionStatus.fromStringValue(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 != null)
			return ((InspectionStatus) arg2).getStringValue();
		return null;
	}

}
