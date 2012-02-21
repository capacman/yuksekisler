package com.yuksekisler.web;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.domain.work.WorkDefinition;

public class WorkDefinitionViewController extends
		AbstractWorkDefinitionController {
	static final Logger LOGGER = LoggerFactory
			.getLogger(WorkDefinitionViewController.class);
	protected LazyDataModel<WorkDefinition> model;

	public LazyDataModel<WorkDefinition> getModel() {
		if (model == null) {
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
		return model;
	}
}
