package com.yuksekisler.application;

import com.yuksekisler.domain.equipment.InspectionReport;

public interface EquipmentService extends CrudService {

	InspectionReport saveInspectionReport(Long reportEquipmentId,
			InspectionReport report, String uploadedUUID);

}
