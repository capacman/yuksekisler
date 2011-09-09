package com.yuksekisler.interfaces.web.jsf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;

public class EmployeeController {
	private static Logger LOGGER = LoggerFactory
			.getLogger(EmployeeController.class);
	private EmployeeService employeeService;
	private Employee employee = new Employee();

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String addEmployee() {
		LOGGER.debug("addEmployee called and employee: {}", employee);
		employeeService.saveEmployee(employee);
		LOGGER.debug("employee added");
		return "success";
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
