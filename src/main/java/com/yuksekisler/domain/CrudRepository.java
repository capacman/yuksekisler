package com.yuksekisler.domain;

import java.util.List;

import com.yuksekisler.application.QueryParameters;

public interface CrudRepository<ID, E extends IdEnabledEntity<ID>> {
	void removeEntity(E e);

	E saveEntity(E e);

	E getEntity(ID id, Class<E> clazz);

	List<E> query(QueryParameters parameters, Class<E> clazz);
}
