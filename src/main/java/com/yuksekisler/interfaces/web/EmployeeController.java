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
import com.yuksekisler.domain.employee.CertificateType;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeTitle;

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

	@RequestMapping(value = "/title/{id}", method = RequestMethod.GET)
	public @ResponseBody
	EmployeeTitle getTitle(@PathVariable("id") Long id) {
		return employeeService.getTitle(id);
	}

	@RequestMapping(value = "/title", method = RequestMethod.POST)
	public @ResponseBody
	EmployeeTitle storeTitle(@RequestBody EmployeeTitle employeeTitle) {
		LOGGER.debug("store employeeTitle: {}", employeeTitle);
		return employeeService.saveTitle(employeeTitle);
	}

	@RequestMapping(value = "/title/{id}", method = RequestMethod.DELETE)
	public void deleteTitle(@PathVariable("id") Long id) {
		employeeService.removeTitle(id);
	}

	@RequestMapping(value = "/title", method = RequestMethod.GET)
	public @ResponseBody
	List<EmployeeTitle> queryTitle(HttpServletRequest request) {
		return employeeService.getTitles();
	}

	@RequestMapping(value = "/certificate/{id}", method = RequestMethod.GET)
	public @ResponseBody
	CertificateType getCertificate(@PathVariable("id") Long id) {
		return employeeService.getCertificate(id);
	}

	@RequestMapping(value = "/certificate", method = RequestMethod.POST)
	public @ResponseBody
	CertificateType storeCertificate(
			@RequestBody CertificateType certificateType) {
		LOGGER.debug("store certificateType: {}", certificateType);
		return employeeService.saveCertificate(certificateType);
	}

	@RequestMapping(value = "/certificate/{id}", method = RequestMethod.DELETE)
	public void deleteCertificate(@PathVariable("id") Long id) {
		employeeService.removeCertificate(id);
	}

	@RequestMapping(value = "/certificate", method = RequestMethod.GET)
	public @ResponseBody
	List<CertificateType> queryCertificate(HttpServletRequest request) {
		return employeeService.getCertificates();
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
