package com.yuksekisler.interfaces.web;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.work.WorkDefinition;

@RequestMapping("/work")
public class WorkController extends
		AbstractBaseCrudController<Long, WorkDefinition> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkController.class);
	private WorkService service;

	@InitBinder({ "startDate", "endDate" })
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					LOGGER.debug("starting to parse value: {}", value);
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					LOGGER.warn("web data binder failed for value: {}", value);
					setValue(null);
				}
			}

			public String getAsText() {
				return new SimpleDateFormat("dd/MM/yyyy")
						.format((Date) getValue());
			}

		});

	}

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

	@RequestMapping(method = RequestMethod.POST, headers = "JsonStore=false")
	public @ResponseBody
	WorkDefinition updateOrCreate(
			@RequestParam(value = "id", required = false) Long workId,
			@RequestParam("name") String name,
			@RequestParam("customer") String customer,
			@RequestParam("startDate") Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate,
			@RequestParam("supervisors") List<Long> supervisors,
			@RequestParam(value = "workers", required = false) List<Long> workers,
			@RequestParam(value = "equipments", required = false) List<Long> equipments) {
		if (workId == null)
			return service.saveWorkDefinition(name, customer, startDate,
					endDate, equipments == null ? new ArrayList<Long>(1)
							: equipments, supervisors,
					workers == null ? new ArrayList<Long>(1) : workers);
		else
			return service.saveWorkDefinition(workId, name, customer,
					startDate, endDate,
					equipments == null ? new ArrayList<Long>(1) : equipments,
					supervisors, workers == null ? new ArrayList<Long>(1)
							: workers);
	}

	public void setWorkService(WorkService service) {
		this.service = service;
	}

}
