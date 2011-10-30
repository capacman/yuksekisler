package com.yuksekisler.application;

import java.util.Date;
import java.util.List;

import com.yuksekisler.domain.work.WorkDefinition;

public interface WorkService extends CrudService {

	WorkDefinition saveWorkDefinition(String name, String customer,
			Date startDate, Date endDate, List<Long> equipmentIDs,
			List<Long> supervisorIDs, List<Long> workerIDs);

	WorkDefinition saveWorkDefinition(Long workID, String name,
			String customer, Date startDate, Date endDate,
			List<Long> equipmentIDs, List<Long> supervisorIDs,
			List<Long> workerIDs);

}
