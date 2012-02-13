package com.yuksekisler.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;

@FacesConverter(forClass = Employee.class)
public class EmployeeConverter implements Converter {
	private EmployeeService getService(FacesContext fc) {
		return FacesContextUtils.getWebApplicationContext(fc).getBean(
				EmployeeService.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return getService(context).getEntity(Long.parseLong(value),
				Employee.class);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Employee) value).getId().toString();
	}

}
