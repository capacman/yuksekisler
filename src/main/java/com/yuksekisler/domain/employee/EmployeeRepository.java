package com.yuksekisler.domain.employee;

import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.HasName;

public interface EmployeeRepository extends BaseRepository {
	Employee findByEmail(String email);
}
