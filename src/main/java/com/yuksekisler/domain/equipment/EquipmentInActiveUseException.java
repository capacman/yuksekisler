package com.yuksekisler.domain.equipment;

import com.yuksekisler.domain.YuksekislerException;

public class EquipmentInActiveUseException extends YuksekislerException {

	private Equipment entity;

	public EquipmentInActiveUseException(Equipment entity) {
		super("equipment " + entity + " is in active use and cannot be deleted");
		this.entity = entity;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1250233499469343629L;

	public Equipment getEntity() {
		return entity;
	}

}
