package com.yuksekisler.web;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.work.WorkDefinition;

public abstract class AbstractWorkDefinitionController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractWorkDefinitionController.class);
	protected WorkService workService;
	protected WorkDefinition work = new WorkDefinition();
	protected Date startDate;
	protected Date endDate;
	protected Long workId;

	public AbstractWorkDefinitionController() {
		super();
	}

	public WorkService getWorkService() {
		return workService;
	}

	public void setWorkService(final WorkService workService) {
		this.workService = workService;
	}

	public WorkDefinition getWork() {
		return work;
	}

	public void setWork(WorkDefinition work) {
		this.work = work;
	}

	public Date getStartDate() {
		if (startDate == null)
			startDate = new Date();
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getWorkId() {
		return workId;
	}

	public void setWorkId(Long workId) {
		this.workId = workId;
	}

	public void loadWork() {
		if (workId != null) {
			work = workService.getEntity(workId, WorkDefinition.class);
			startDate = work.getLifeTime().getStartDate();
			endDate = work.getLifeTime().getEndDate();
			LOGGER.info("work equipment size:{}", work.getEquipments().size());
		} else {
			LOGGER.info("workId is null");
		}
	}

}