package com.yuksekisler.domain;

import java.util.List;

public interface BaseRepository<ID, E extends IdEnabledEntity<ID>> extends
		CrudRepository<ID, E> {

	E merge(E entity);

	void clear();

	void flush();

	List<E> findEntries(int firstResult, int maxResults, Class<E> clazz);

	List<E> findAll(Class<E> clazz);

	long countEntries(Class<E> clazz);

}