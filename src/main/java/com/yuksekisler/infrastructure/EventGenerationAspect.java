package com.yuksekisler.infrastructure;

import org.aspectj.lang.ProceedingJoinPoint;

import com.yuksekisler.application.NotificationService;
import com.yuksekisler.domain.equipment.InspectionReport;

public class EventGenerationAspect {
	public NotificationService notificationService;

	public Object aroundsaveInspectionReport(ProceedingJoinPoint pjp)
			throws Throwable {
		InspectionReport value = (InspectionReport) pjp.proceed();
		notificationService.onInspectionReport(value);
		return value;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
}
