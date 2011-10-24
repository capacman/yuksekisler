package com.yuksekisler.domain.work;

import java.util.Date;

public class LifeTime {

	private Date startDate;
	private Date endDate;

	public LifeTime(Date startDate, Date endDate) {
		super();
		if (startDate == null)
			throw new IllegalArgumentException(
					"LifeTime must have at least startDate parameter not null!");
		this.startDate = new Date(startDate.getTime());
		this.endDate = endDate == null ? null : new Date(endDate.getTime());
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public boolean isConflictedWith(LifeTime lifeTime) {
		if (endDate != null && endDate.before(lifeTime.startDate))
			return false;
		if (lifeTime.endDate != null && lifeTime.endDate.before(startDate))
			return false;
		return true;
	}

	public boolean isFinished() {
		return endDate == null ? false : endDate.before(new Date());
	}
}
