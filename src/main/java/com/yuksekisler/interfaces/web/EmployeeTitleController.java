package com.yuksekisler.interfaces.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.EmployeeTitle;

@RequestMapping("/employee/title")
public class EmployeeTitleController extends
		AbstractBaseCrudController<Long, EmployeeTitle> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeTitleController.class);
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

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
