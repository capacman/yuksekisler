package com.yuksekisler.domain;

import java.io.Serializable;

public interface IdEnabledEntity extends Serializable{
	Long getId();
	Boolean getErased();
	void setErased(Boolean value);
}