package com.yuksekisler.domain;

import java.util.List;

import com.yuksekisler.application.QueryParameters;

public interface CrudRepository {
	// <ID, E extends IdEnabledEntity<ID>>
	<E extends IdEnabledEntity<?>> void removeEntity(E e);

	<E extends IdEnabledEntity<?>> E saveEntity(E e);

	<ID, E extends IdEnabledEntity<ID>> E getEntity(ID id, Class<E> clazz);

	<E extends IdEnabledEntity<?>> List<E> query(QueryParameters parameters,
			Class<E> clazz, final String searchAttribute);
}
