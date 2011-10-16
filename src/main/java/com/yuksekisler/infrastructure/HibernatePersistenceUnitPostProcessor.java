package com.yuksekisler.infrastructure;

import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

public class HibernatePersistenceUnitPostProcessor implements
		PersistenceUnitPostProcessor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HibernatePersistenceUnitPostProcessor.class);
	private Properties persistenceUnitProperties;

	@Override
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		LOGGER.debug("starting process persistentUnit {}",
				pui.getPersistenceUnitName());
		for (Entry<Object, Object> entry : persistenceUnitProperties.entrySet()) {
			pui.addProperty((String) entry.getKey(), (String) entry.getValue());
			LOGGER.debug("property key : {} value : {} added", entry.getKey(),
					entry.getValue());
		}
	}

	public void setPersistenceUnitProperties(
			Properties persistenceUnitProperties) {
		this.persistenceUnitProperties = persistenceUnitProperties;
	}
}
