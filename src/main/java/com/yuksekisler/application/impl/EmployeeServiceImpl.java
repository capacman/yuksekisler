package com.yuksekisler.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.HasName;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeRepository;

public class EmployeeServiceImpl extends AbstractBaseCrudService implements
		EmployeeService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeServiceImpl.class);
	private EmployeeRepository repository;

	public void setRepository(EmployeeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Employee getEmployeeByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public EmployeeRepository getRepository() {
		return repository;
	}

}
