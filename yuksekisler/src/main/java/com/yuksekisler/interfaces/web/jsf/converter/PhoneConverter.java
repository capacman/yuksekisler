package com.yuksekisler.interfaces.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.yuksekisler.domain.employee.Phone;

@FacesConverter(forClass = Phone.class)
public class PhoneConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent ui, String val) {
		return new Phone(val);
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent ui, Object val) {
		if (val != null)
			return ((Phone) val).getPhoneNumber();
		return "";
	}
}
