package com.yuksekisler.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ExceptionMailNotifier {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExceptionMailNotifier.class);
	private static final Marker NOTIFY_ADMIN = MarkerFactory
			.getMarker("NOTIFY_ADMIN");

	public void scheduleErrorMails() {
		LOGGER.warn(NOTIFY_ADMIN, "dummy");
	}
}
