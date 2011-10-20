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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractBaseCrudController<ID, E extends IdEnabledEntity<ID>>
		implements CrudController<ID, E> {
	public static final Pattern SORT_PATTERN = Pattern
			.compile("sort\\(([+\\s-])(.*)\\)");
	public static final Pattern RANGE_PATTERN = Pattern
			.compile("items=(\\d*)-(\\d*)");

	public AbstractBaseCrudController() {
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
				continue;
			}
			parameters.addParameter(parameterEntry.getKey(),
					parameterEntry.getValue()[0]);
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

	public String getTypeName() {
		return getEntityClass().getName().toUpperCase();
	}

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

	abstract protected Class<E> getEntityClass();

	abstract protected CrudService getService();

	public E get(ID id) {
		return getService().getEntity(id, getEntityClass());
	}

	@Override
	public E store(E entity) {
		return getService().saveEntity(entity);
	}

	@Override
	public void delete(ID id) {
		getService().removeEntity(id, getEntityClass());
	}

	@Override
	public ResponseEntity<List<E>> query(HttpServletRequest request) {
		QueryParameters parameters = prepareQueryParameters(request);
		if ((parameters.getSearchString() != null && !parameters
				.getSearchString().isEmpty())
				|| parameters.hasRange()
				|| parameters.hasOrder()) {
			List<E> queryResult = getService().query(parameters,
					getEntityClass(), getSearchAttribute());
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.add(
					"Content-Range",
					"items "
							+ parameters.getRangeStart()
							+ "-"
							+ (parameters.getRangeStart() + queryResult.size() < parameters
									.getRangeEnd() ? parameters.getRangeStart()
									+ queryResult.size() : parameters
									.getRangeEnd()) + "/"
							+ getService().getEntityCount(getEntityClass()));
			ResponseEntity<List<E>> responseEntity = new ResponseEntity<List<E>>(
					queryResult, headers, HttpStatus.OK);
			return responseEntity;
		} else {
			return new ResponseEntity<List<E>>(getService().getAllEntities(
					getEntityClass()), HttpStatus.OK);
		}
	}

	abstract protected String getSearchAttribute();

}