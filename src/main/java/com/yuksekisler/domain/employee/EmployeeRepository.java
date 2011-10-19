package com.yuksekisler.domain.employee;

import com.yuksekisler.domain.BaseRepository;

public interface EmployeeRepository extends BaseRepository<Long, Employee> {
	Employee findByEmail(String email);
}
