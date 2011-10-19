package com.yuksekisler.domain;

import java.io.Serializable;

public interface IdEnabledEntity<ID> extends Serializable {
	ID getId();

	Boolean getErased();

	void setErased(Boolean value);
}