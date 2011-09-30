package com.yuksekisler.infrastructure;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

public class ViewAwareObjectMapper extends ObjectMapper {
	public ViewAwareObjectMapper() {
		SimpleFilterProvider provider = new SimpleFilterProvider();
		provider.addFilter("employee",
				SimpleBeanPropertyFilter.filterOutAllExcept("name"));
		setSerializationConfig(getSerializationConfig().withFilters(provider));
	}
}
