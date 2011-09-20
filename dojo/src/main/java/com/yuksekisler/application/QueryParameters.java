package com.yuksekisler.application;


public class QueryParameters {
	private String searchString;
	private String orderByField;
	private boolean ascending;

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

	@Override
	public String toString() {
		return "QueryParameters [searchString=" + searchString
				+ ", orderByField=" + orderByField + ", ascending=" + ascending
				+ "]";
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
