package com.yuksekisler.interfaces.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.EmployeeTitle;

@RequestMapping("/employee/title")
public class EmployeeTitleController extends
		AbstractBaseCrudController<Long, EmployeeTitle> {

	private EmployeeService employeeService;

	@Override
	protected Class<EmployeeTitle> getEntityClass() {
		return EmployeeTitle.class;
	}

	@Override
	protected CrudService getService() {
		return employeeService;
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
