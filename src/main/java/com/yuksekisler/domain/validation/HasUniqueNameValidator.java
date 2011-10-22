package com.yuksekisler.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.domain.HasName;

public class HasUniqueNameValidator implements
		ConstraintValidator<HasUniqueName, HasName<?>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HasUniqueNameValidator.class);
	private CrudService crudService;

	@Override
	public void initialize(HasUniqueName constraintAnnotation) {

	}

	@Override
	public boolean isValid(HasName<?> value, ConstraintValidatorContext context) {
		LOGGER.debug("constraint validator validating with name {}",
				value.getName());
		assert getCrudService() != null;
		return value.getErased()
				|| getCrudService().findByName(value.getName(),
						value.getClass()).isEmpty();
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
