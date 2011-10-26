package com.yuksekisler.application;

import com.yuksekisler.domain.equipment.InspectionReport;

public interface NotificationService {

	public void onInspectionReport(InspectionReport inspectionReport);
}
