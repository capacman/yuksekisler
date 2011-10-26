package com.yuksekisler.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.NotificationService;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.equipment.InspectionStatus;

public class NotificationServiceImpl implements NotificationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotificationServiceImpl.class);
	private EquipmentService equipmentService;

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Override
	public void onInspectionReport(InspectionReport inspectionReport) {
		if (inspectionReport.getStatus() == InspectionStatus.NOTUSABLE) {
			// Equipment equipment = equipmentService
			// .findByInspectionReport(inspectionReport);
			LOGGER.info("inspection report detected: "
					+ inspectionReport.toString());
		}
	}
}
