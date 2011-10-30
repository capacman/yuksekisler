package com.yuksekisler.application.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.work.WorkDefinition;
import com.yuksekisler.domain.work.WorkRepository;

public class WorkServiceImpl extends AbstractBaseCrudService implements
		WorkService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkServiceImpl.class);
	private WorkRepository workRepository;

	@Override
	public BaseRepository getRepository() {
		return workRepository;
	}

	public void setWorkRepository(WorkRepository workRepository) {
		this.workRepository = workRepository;
	}

	@Override
	public WorkDefinition saveWorkDefinition(String name, String customer,
			Date startDate, Date endDate, List<Long> equipmentIDs,
			List<Long> supervisorIDs, List<Long> workerIDs) {
		WorkDefinition work = new WorkDefinition();
		updateWork(name, customer, startDate, endDate, equipmentIDs,
				supervisorIDs, workerIDs, work);
		return saveEntity(work);
	}

	@Override
	public WorkDefinition saveWorkDefinition(Long workID, String name,
			String customer, Date startDate, Date endDate,
			List<Long> equipmentIDs, List<Long> supervisorIDs,
			List<Long> workerIDs) {
		WorkDefinition work = workRepository.getEntity(workID,
				WorkDefinition.class);
		updateWork(name, customer, startDate, endDate, equipmentIDs,
				supervisorIDs, workerIDs, work);
		return saveEntity(work);
	}

	private void updateWork(String name, String customer, Date startDate,
			Date endDate, List<Long> equipmentIDs, List<Long> supervisorIDs,
			List<Long> workerIDs, WorkDefinition work) {
		work.setName(name);
		work.setCustomer(customer);
		work.setStartDate(startDate);
		work.setEndDate(endDate);
		LOGGER.debug(
				"equipments will be added to workDefinition with lifeTime: {}",
				work.getLifeTime());
		work.removeEquipments();
		for (Equipment e : workRepository.getEntities(equipmentIDs,
				Equipment.class)) {
			work.addEquipment(e);
		}
		work.removeSupervisors();
		for (Employee e : workRepository.getEntities(supervisorIDs,
				Employee.class)) {
			work.addSupervisor(e);
		}
		work.removeWorkers();
		for (Employee e : workRepository.getEntities(workerIDs, Employee.class)) {
			work.addWorker(e);
		}
	}
}
