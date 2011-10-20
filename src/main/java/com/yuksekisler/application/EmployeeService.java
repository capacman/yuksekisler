package com.yuksekisler.application;

import com.yuksekisler.domain.employee.Employee;

public interface EmployeeService extends CrudService {

	Employee getEmployeeByEmail(String email);

}
