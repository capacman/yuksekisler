package com.yuksekisler.application.impl;

import com.yuksekisler.application.WorkService;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.work.WorkRepository;

public class WorkServiceImpl extends AbstractBaseCrudService implements
		WorkService {
	private WorkRepository workRepository;

	@Override
	public BaseRepository getRepository() {
		return workRepository;
	}

	public void setWorkRepository(WorkRepository workRepository) {
		this.workRepository = workRepository;
	}
}
