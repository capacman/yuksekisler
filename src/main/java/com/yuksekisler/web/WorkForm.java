package com.yuksekisler.web;

import java.util.Date;

import com.yuksekisler.domain.work.WorkDefinition;

public interface WorkForm {

	public abstract WorkDefinition getWork();

	public abstract void setWork(WorkDefinition work);

	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract Long getWorkId();

	public abstract void setWorkId(Long workId);

}