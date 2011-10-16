package com.yuksekisler.domain.equipment;

public enum InspectionStatus {

	USABLE("USABLE"), NOTUSABLE("NOTUSABLE"), FIXED("FIXED");
	private final String strValue;

	private InspectionStatus(String strValue) {
		this.strValue = strValue;
	}

	public String getStringValue() {
		return strValue;
	}

	public static InspectionStatus fromStringValue(String value) {
		if (USABLE.getStringValue().equalsIgnoreCase(value))
			return USABLE;
		if (NOTUSABLE.getStringValue().equalsIgnoreCase(value))
			return NOTUSABLE;
		if (FIXED.getStringValue().equalsIgnoreCase(value))
			return FIXED;
		throw new IllegalArgumentException(
				"There is no status with string representation " + value);
	}
}
