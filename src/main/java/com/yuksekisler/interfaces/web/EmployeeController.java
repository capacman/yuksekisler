package com.yuksekisler.interfaces.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;

@RequestMapping("/employee")
public class EmployeeController extends
		AbstractBaseCrudController<Long, Employee> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeController.class);
	private EmployeeService employeeService;

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	protected Class<Employee> getEntityClass() {
		return Employee.class;
	}

	@Override
	protected CrudService getService() {
		return employeeService;
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}
}
