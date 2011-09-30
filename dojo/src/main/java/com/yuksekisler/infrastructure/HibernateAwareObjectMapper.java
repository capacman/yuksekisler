package com.yuksekisler.infrastructure;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.fasterxml.jackson.module.hibernate.HibernateModule;


public class HibernateAwareObjectMapper extends ObjectMapper {
	public HibernateAwareObjectMapper() {
		HibernateModule hm = new HibernateModule();
		registerModule(hm);
		configure(Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	public void setPrettyPrint(boolean prettyPrint) {
		configure(Feature.INDENT_OUTPUT, prettyPrint);
	}
}
