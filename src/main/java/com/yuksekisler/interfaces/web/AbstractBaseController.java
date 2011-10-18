package com.yuksekisler.interfaces.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yuksekisler.application.QueryParameters;

public abstract class AbstractBaseController {
	public static final Pattern SORT_PATTERN = Pattern
			.compile("sort\\(([+\\s-])(.*)\\)");
	public static final Pattern RANGE_PATTERN = Pattern
			.compile("items=(\\d*)-(\\d*)");

	public AbstractBaseController() {
		super();
	}

	public QueryParameters prepareQueryParameters(HttpServletRequest request) {
		Map parameterMap = request.getParameterMap();
		QueryParameters parameters = new QueryParameters();
		for (Object parameterObject : parameterMap.entrySet()) {
			Entry<String, String[]> parameterEntry = (Entry) parameterObject;
			if (parameterEntry.getKey().equalsIgnoreCase("ajaxRequest"))
				continue;
			if (parameterEntry.getKey().startsWith("sort")) {
				Matcher matcher = SORT_PATTERN.matcher(parameterEntry.getKey());
				boolean matches = matcher.matches();
				assert matches;
				String order = matcher.group(1);
				String field = matcher.group(2);
				parameters.setOrder(field, order.equalsIgnoreCase("+"));
				continue;
			}
			if (parameterEntry.getKey().equalsIgnoreCase("searchString")) {
				parameters.setSearchString(parameterEntry.getValue()[0]);
			}
		}
		if (request.getHeader("Range") != null) {
			Matcher matcher = RANGE_PATTERN.matcher(request.getHeader("Range"));
			boolean matches = matcher.matches();
			assert matches;
			int start = Integer.parseInt(matcher.group(1));
			int end = Integer.parseInt(matcher.group(2));
			parameters.setRange(start, end);
		}
		return parameters;
	}

	public abstract String getTypeName();

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public Map<String, String> handleNoEquipment() {
		Map<String, String> errorResponse = new HashMap<String, String>();
		errorResponse.put("type", getTypeName());
		return errorResponse;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	public Map<String, ?> handleConstraintValidation(
			ConstraintViolationException e) {
		Map<String, Object> errorResponse = new HashMap<String, Object>();
		errorResponse.put("type", getTypeName());
		List<Map<String, Object>> violations = new ArrayList<Map<String, Object>>();
		errorResponse.put("violations", violations);
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			Map<String, Object> innerResponse = new HashMap<String, Object>();
			innerResponse.put("violatedType", violation.getLeafBean()
					.getClass().getCanonicalName());
			innerResponse.put("path", violation.getPropertyPath().toString());
			violations.add(innerResponse);
		}
		return errorResponse;
	}
}