package com.yuksekisler.application;

import java.util.Date;
import java.util.List;

import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeIdentity;
import com.yuksekisler.domain.employee.EmployeeTitle;

public interface EmployeeService {
	Employee createEmployee(String name, String email, String password,
			EmployeeTitle title, EmployeeIdentity employeeIdentity,
			Date startDate);

	Employee saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployee(Long id);

	Employee getEmployeeByEmail(String email);

	List<EmployeeTitle> getTitles();

	EmployeeTitle getTitle(Long id);

	EmployeeTitle saveTitle(EmployeeTitle title);
}
