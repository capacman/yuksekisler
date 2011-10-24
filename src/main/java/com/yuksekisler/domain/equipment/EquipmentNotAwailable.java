package com.yuksekisler.domain.equipment;

import java.util.Date;

import com.yuksekisler.domain.YuksekislerException;

public class EquipmentNotAwailable extends YuksekislerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4644651392613236892L;
	private Equipment equipment;
	private Date startDate;
	private Date endDate;

	public EquipmentNotAwailable(Equipment equipment, Date startDate,
			Date endDate) {
		super("equipment " + equipment
				+ " not available between Dates startDate: " + startDate
				+ " and endDate: " + endDate);
		this.equipment = equipment;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

}
