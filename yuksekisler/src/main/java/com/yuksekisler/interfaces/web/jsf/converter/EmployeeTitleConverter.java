package com.yuksekisler.interfaces.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.EmployeeTitle;

@FacesConverter(forClass = EmployeeTitle.class)
public class EmployeeTitleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent uiComponent, String value) {
		return getService(facesContext).getTitle(Long.parseLong(value));
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object title) {
		return ((EmployeeTitle) title).getId().toString();
	}

	private EmployeeService getService(FacesContext fc) {
		return FacesContextUtils.getWebApplicationContext(fc).getBean(
				EmployeeService.class);
	}

}
