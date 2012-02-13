package com.yuksekisler.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.yuksekisler.domain.equipment.InspectionStatus;

@FacesConverter(forClass = InspectionStatus.class)
public class InspectionStatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return InspectionStatus.fromStringValue(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((InspectionStatus) value).getStringValue();
	}

}
