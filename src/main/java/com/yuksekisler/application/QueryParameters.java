package com.yuksekisler.application;

public class QueryParameters {
	private String searchString;
	private String orderByField;
	private boolean ascending;
	private int rangeStart = -1;
	private int rangeEnd = -1;

	public void setOrder(String orderField, boolean ascending) {
		this.orderByField = orderField;
		this.ascending = ascending;
	}

	public boolean hasOrder() {
		if (orderByField == null || orderByField.isEmpty())
			return false;
		return true;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public boolean isAscending() {
		return ascending;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void setRange(int start, int end) {
		if (start < 0 || end < 0 || start >= end) {
			throw new IllegalArgumentException(
					"Could not set range parameters with values start: "
							+ start + " end: " + end);
		}
		this.rangeStart = start;
		this.rangeEnd = end;
	}

	public int getRangeStart() {
		return rangeStart;
	}

	public int getRangeEnd() {
		return rangeEnd;
	}

	public boolean hasRange() {
		return rangeStart != -1 && rangeEnd != -1;
	}

	@Override
	public String toString() {
		return "QueryParameters [searchString=" + searchString
				+ ", orderByField=" + orderByField + ", ascending=" + ascending
				+ ", rangeStart=" + rangeStart + ", rangeEnd=" + rangeEnd + "]";
	}

}