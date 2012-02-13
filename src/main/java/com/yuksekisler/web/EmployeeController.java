package com.yuksekisler.web;

import java.util.List;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.CertificateType;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeTitle;

public class EmployeeController {
	private EmployeeService employeeService;

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public List<EmployeeTitle> getTitles() {
		return employeeService.getAllEntities(EmployeeTitle.class);
	}

	public List<CertificateType> getCertificateTypes() {
		return employeeService.getAllEntities(CertificateType.class);
	}

	public List<Employee> getEmployees() {
		return employeeService.getAllEntities(Employee.class);
	}
}
