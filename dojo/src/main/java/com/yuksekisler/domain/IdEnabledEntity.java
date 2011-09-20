package com.yuksekisler.domain;

public interface IdEnabledEntity {
	Long getId();
	Boolean getEnabled();
	void setEnabled(Boolean value);
}