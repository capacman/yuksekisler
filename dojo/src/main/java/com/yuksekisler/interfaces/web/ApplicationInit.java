package com.yuksekisler.interfaces.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuksekisler.application.EmployeeService;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeIdentity;
import com.yuksekisler.domain.employee.EmployeeTitle;
import com.yuksekisler.domain.employee.IdentityType;
import com.yuksekisler.domain.employee.Phone;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.equipment.InspectionStatus;
import com.yuksekisler.infrastructure.security.GrantedAuthorityImpl;

public class ApplicationInit {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ApplicationInit.class);
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EquipmentService equipmentService;

	@PostConstruct
	public void init() {
		try {
			employeeService.getEmployeeByEmail("achalil@gmail.com");
		} catch (NoResultException e) {
			LOGGER.warn("could not find default user");
			LOGGER.info("registering default user");
			Employee employee = new Employee();
			employee.setAccountNonExpired(true);
			employee.setAccountNonLocked(true);
			employee.setCredentialsNonExpired(true);
			employee.setEmail("achalil@gmail.com");
			employee.setEmployeeIdentity(new EmployeeIdentity("123456789",
					IdentityType.VKN));
			employee.setErased(false);
			employee.setName("anil chalil");
			employee.setPassword("123456");
			employee.setPhone(new Phone("905316622926"));
			employee.setStartDate(new Date());
			EmployeeTitle title = getTitle();
			employee.setTitle(title);
			employee.getAuthorities().add(GrantedAuthorityImpl.USER);
			employee.getAuthorities().add(GrantedAuthorityImpl.ADMIN);
			employee = employeeService.saveEmployee(employee);

			Brand brand = equipmentService.createNewBrand(new Brand("a", "a"));
			Category category = equipmentService
					.createNewCategory(new Category("a", "a"));
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -10);
			Date entrance = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 5);
			Date production = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 10);
			Date bestBefore = calendar.getTime();

			for (int i = 1; i <= 30; i++) {
				Equipment equipment = equipmentService.createEquipment(
						category, brand, entrance, bestBefore, production,
						Integer.toString(i), "product" + i);
				for (int j = 1; j <= 5; j++)
					equipment.addInspectionReport(new InspectionReport(
							employee, new Date(), "some content",
							InspectionStatus.USABLE));
				equipmentService.saveEquipment(equipment);
			}
		}
	}

	public EmployeeTitle getTitle() {
		List<EmployeeTitle> titles = employeeService.getTitles();
		EmployeeTitle title;
		if (titles.isEmpty()) {
			title = new EmployeeTitle("foo", "bar");
			title = employeeService.saveTitle(title);
			title = new EmployeeTitle("foo1", "bar1");
			title = employeeService.saveTitle(title);
		} else {
			title = titles.get(0);
		}
		return title;
	}
	// public EmployeeService getEmployeeService() {
	// return employeeService;
	// }
	//
	// public void setEmployeeService(EmployeeService employeeService) {
	// this.employeeService = employeeService;
	// }
}