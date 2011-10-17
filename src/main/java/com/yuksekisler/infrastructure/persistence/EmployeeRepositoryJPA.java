package com.yuksekisler.infrastructure.persistence;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeRepository;

@Repository
public class EmployeeRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EmployeeRepository {

	@Override
	public Employee findByEmail(String email) {
		TypedQuery<Employee> query = entityManager
				.createQuery(
						"select o from Employee o where o.email = :email and o.erased=false",
						Employee.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}

}
