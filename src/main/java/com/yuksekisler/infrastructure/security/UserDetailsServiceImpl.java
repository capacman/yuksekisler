package com.yuksekisler.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.domain.employee.Employee;

public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDetailsServiceImpl.class);
	private EmployeeService employeeService;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		LOGGER.debug("username with {} requested!", userName);
		Employee employeeByEmail = employeeService.getEmployeeByEmail(userName);
		LOGGER.debug("user found {}", employeeByEmail);
		return employeeByEmail;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
