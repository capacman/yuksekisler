package com.yuksekisler.interfaces.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;

@RequestMapping("/employee")
public class EmployeeController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeController.class);
	private EmployeeService employeeService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Employee get(@PathVariable("id") Long id) {
		return employeeService.getEmployee(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	Employee store(@RequestBody Employee employee) {
		LOGGER.debug("store employee: {}", employee);
		return employeeService.saveEmployee(employee);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		employeeService.removeEmployee(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<Employee> query(HttpServletRequest request) {
		return employeeService.getAllEmployees();
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
