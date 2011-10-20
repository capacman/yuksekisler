package com.yuksekisler.domain;

public interface HasName<ID> extends IdEnabledEntity<ID> {
	String getName();
}
