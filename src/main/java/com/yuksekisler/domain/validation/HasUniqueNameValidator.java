package com.yuksekisler.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.domain.HasName;

public class HasUniqueNameValidator implements
		ConstraintValidator<HasUniqueName, HasName<?>> {

	private CrudService crudService;

	@Override
	public void initialize(HasUniqueName constraintAnnotation) {

	}

	@Override
	public boolean isValid(HasName<?> value, ConstraintValidatorContext context) {
		assert getCrudService() != null;
		try {
			return getCrudService().findByName(value.getName(),
					value.getClass()).isEmpty();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;

	}

	public CrudService getCrudService() {
		return crudService;
	}

	@Qualifier("employeeService")
	@Autowired(required = true)
	public void setCrudService(CrudService crudService) {
		System.out.println("crudService injected");
		this.crudService = crudService;
	}
}
