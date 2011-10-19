package com.yuksekisler.application;

import java.util.List;

import com.yuksekisler.domain.IdEnabledEntity;

public interface CrudService {
	<E extends IdEnabledEntity<?>> E removeEntity(E e);

	<E extends IdEnabledEntity<?>> E saveEntity(E e);

	<ID, E extends IdEnabledEntity<ID>> E getEntity(ID id, Class<E> clazz);

	<E extends IdEnabledEntity<?>> List<E> query(QueryParameters parameters,
			Class<E> clazz);
}
