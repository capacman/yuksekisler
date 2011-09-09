package com.yuksekisler.application.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeIdentity;
import com.yuksekisler.domain.employee.EmployeeRepository;
import com.yuksekisler.domain.employee.EmployeeTitle;

public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeServiceImpl.class);
	private EmployeeRepository repository;

	public void setRepository(EmployeeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Employee createEmployee(String name, String email, String password,
			EmployeeTitle title, EmployeeIdentity employeeIdentity,
			Date startDate) {
		Employee employee = new Employee();
		employee.setName(name);
		employee.setEmail(email);
		employee.setPassword(password);
		employee.setTitle(title);
		employee.setEmployeeIdentity(employeeIdentity);
		employee.setStartDate(startDate);
		repository.persist(employee);
		return employee;
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		try {
			return repository.merge(employee);
		} catch (IllegalArgumentException e) {
			LOGGER.debug("employee could not find persist it", e);
		}
		repository.persist(employee);
		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return repository.findAll(Employee.class);
	}

	@Override
	public Employee getEmployee(Long id) {
		return repository.find(id, Employee.class);
	}

	@Override
	public Employee getEmployeeByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public List<EmployeeTitle> getTitles() {
		return repository.findAll(EmployeeTitle.class);
	}

	@Override
	public EmployeeTitle saveTitle(EmployeeTitle title) {
		try {
			return repository.merge(title);
		} catch (IllegalArgumentException e) {
			LOGGER.debug("title could not find persist it", e);
		}
		repository.persist(title);
		return title;
	}

	@Override
	public EmployeeTitle getTitle(Long id) {
		return repository.find(id, EmployeeTitle.class);
	}

}
