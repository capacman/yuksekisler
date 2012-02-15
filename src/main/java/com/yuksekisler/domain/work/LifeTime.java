package com.yuksekisler.domain.work;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
public class LifeTime {

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDate;

	public LifeTime() {
		this(new Date(), null);
	}

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

	public boolean isActive() {
		Date current = new Date();
		return startDate.before(current)
				&& (endDate == null || endDate.after(current));
	}

	@Override
	public String toString() {
		return "LifeTime [startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}
}
