package com.yuksekisler.domain.work;

import com.yuksekisler.domain.YuksekislerException;

public class WorkAlreadyFinished extends YuksekislerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7880017503271427311L;
	private WorkDefinition workDefinition;

	public WorkAlreadyFinished(WorkDefinition workDefinition) {
		super("work already finished between dates "
				+ workDefinition.getStartDate() + " and "
				+ workDefinition.getEndDate());
		this.workDefinition = workDefinition;
	}

	public WorkDefinition getWorkDefinition() {
		return workDefinition;
	}

}
