package com.yuksekisler.infrastructure;

import ch.qos.logback.classic.boolex.OnMarkerEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;

public class MarkerBasedEvaluater extends OnMarkerEvaluator {
	static int LIMIT = 10;
	int counter = 0;

	public MarkerBasedEvaluater() {
		addMarker("NOTIFY_ADMIN");
	}

	@Override
	public boolean evaluate(ILoggingEvent event) throws NullPointerException,
			EvaluationException {
		boolean returnValue = ++counter % LIMIT == 0 || super.evaluate(event);
		System.out.println("markerBasedEvaluater value " + returnValue);
		return returnValue;
	}
}
