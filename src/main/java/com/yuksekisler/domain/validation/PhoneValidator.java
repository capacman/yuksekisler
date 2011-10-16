package com.yuksekisler.domain.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements
		ConstraintValidator<Phone, com.yuksekisler.domain.employee.Phone> {
	private static final Pattern PATTERN = Pattern.compile("[^0-9]");

	@Override
	public void initialize(Phone arg0) {
		// do nothing

	}

	// TODO: fix later
	@Override
	public boolean isValid(com.yuksekisler.domain.employee.Phone phoneNumber,
			ConstraintValidatorContext arg1) {
		if (phoneNumber == null)
			return true;
		Matcher matcher = PATTERN.matcher(phoneNumber.getPhoneNumber());
		String pureNumbers = matcher.replaceAll("");
		try {
			String postFix = pureNumbers.substring(pureNumbers.length() - 6);
			String prefix = pureNumbers.substring(0, pureNumbers.length() - 6);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
}
