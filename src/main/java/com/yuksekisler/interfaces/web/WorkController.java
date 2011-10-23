package com.yuksekisler.interfaces.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.work.WorkDefinition;

@RequestMapping("/work")
public class WorkController extends
		AbstractBaseCrudController<Long, WorkDefinition> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkController.class);
	private WorkService service;

	@Override
	protected Class<WorkDefinition> getEntityClass() {
		return WorkDefinition.class;
	}

	@Override
	protected CrudService getService() {
		return service;
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	public void setWorkService(WorkService service) {
		this.service = service;
	}

}
