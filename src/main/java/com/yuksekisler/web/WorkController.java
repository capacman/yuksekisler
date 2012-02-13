package com.yuksekisler.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.work.WorkDefinition;

public class WorkController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkController.class);
	private WorkService workService;
	private LazyDataModel<WorkDefinition> model;
	private WorkDefinition work = new WorkDefinition();
	private Date startDate;
	private Date endDate;
	private Long workId;

	public WorkService getWorkService() {
		return workService;
	}

	public void setWorkService(final WorkService workService) {
		this.workService = workService;
		model = new LazyDataModel<WorkDefinition>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1961966247543272506L;

			@Override
			public List<WorkDefinition> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				return workService.getEntities(first, pageSize,
						WorkDefinition.class);
			}

		};
		model.setRowCount((int) workService
				.getEntityCount(WorkDefinition.class));
		model.setPageSize(10);
	}

	public LazyDataModel<WorkDefinition> getModel() {
		return model;
	}

	public WorkDefinition getWork() {
		return work;
	}

	public void setWork(WorkDefinition work) {
		this.work = work;
	}

	public Date getStartDate() {
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
		} else {
			LOGGER.info("workId is null");
		}
	}
}
