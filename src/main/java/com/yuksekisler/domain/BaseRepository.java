package com.yuksekisler.domain;

import java.util.List;

import javax.persistence.metamodel.IdentifiableType;

public interface BaseRepository extends CrudRepository {
	// ID, E extends IdEnabledEntity<ID>
	<E> E merge(E entity);

	void clear();

	void flush();

	<E extends IdEnabledEntity<?>> List<E> findEntries(int firstResult,
			int maxResults, Class<E> clazz);

	<E extends IdEnabledEntity<?>> List<E> findAll(Class<E> clazz);

	<E extends IdEnabledEntity<?>> long countEntries(Class<E> clazz);

	<E> E persist(E entity);

	<E extends HasName<?>> List<E> findByName(String name, Class<E> clazz);

	<ID, E extends IdEnabledEntity<ID>> List<E> getEntities(List<ID> idx,
			Class<E> clazz);

}