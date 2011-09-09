package com.yuksekisler.infrastructure.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yuksekisler.application.EmployeeService;

public class UserDetailsServiceImpl implements UserDetailsService {

	private EmployeeService employeeService;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		return employeeService.getEmployeeByEmail(userName);
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
