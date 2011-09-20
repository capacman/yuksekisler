package com.yuksekisler.interfaces.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yuksekisler.domain.employee.Employee;

@RequestMapping("/user")
public class UserController {
	private UserDetailsService userDetailsService;

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public UserInfo currentUser() {
		Employee employee = (Employee) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return new UserInfo(employee.getName(), employee.getName());
	}
}
