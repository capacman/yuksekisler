package com.yuksekisler.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasUniqueNameValidator.class)
@Documented
public @interface HasUniqueName {
	String message() default "{com.yuksekisler.domain.validation.HasUniqueNameValidator}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
