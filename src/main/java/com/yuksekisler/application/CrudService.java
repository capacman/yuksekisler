package com.yuksekisler.application;

import java.util.List;

import com.yuksekisler.domain.HasName;
import com.yuksekisler.domain.IdEnabledEntity;

public interface CrudService {
	<ID, E extends IdEnabledEntity<ID>> void removeEntity(ID id, Class<E> clazz);

	<E extends IdEnabledEntity<?>> E saveEntity(E e);

	<ID, E extends IdEnabledEntity<ID>> E getEntity(ID id, Class<E> clazz);

	<ID, E extends IdEnabledEntity<ID>> List<E> getAllEntities(Class<E> clazz);

	<E extends IdEnabledEntity<?>> List<E> query(QueryParameters parameters,
			Class<E> clazz, String searchAttribute);

	<E extends IdEnabledEntity<?>> List<E> getEntities(int startIndex,
			int size, Class<E> clazz);

	<E extends IdEnabledEntity<?>> long getEntityCount(Class<E> clazz);

	<E extends HasName<?>> List<E> findByName(String name, Class<E> clazz);
}
