package com.yuksekisler.interfaces.web;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.yuksekisler.application.QueryParameters;

public class AbstractBaseController {
	public static final Pattern SORT_PATTERN = Pattern
			.compile("sort\\(([+\\s-])(.*)\\)");

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
		return parameters;
	}

}