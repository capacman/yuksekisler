package com.yuksekisler.application.impl;

import java.util.List;

import com.yuksekisler.application.CrudService;
import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.HasName;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractBaseCrudService implements CrudService {

	@Override
	public <ID, E extends IdEnabledEntity<ID>> void removeEntity(ID id,
			Class<E> clazz) {
		getRepository().removeEntity(getRepository().getEntity(id, clazz));
	}

	@Override
	public <E extends IdEnabledEntity<?>> E saveEntity(E e) {
		return getRepository().saveEntity(e);
	}

	@Override
	public <ID, E extends IdEnabledEntity<ID>> E getEntity(ID id, Class<E> clazz) {
		return getRepository().getEntity(id, clazz);
	}

	@Override
	public <E extends IdEnabledEntity<?>> List<E> query(
			QueryParameters parameters, Class<E> clazz, String searchAttribute) {
		return getRepository().query(parameters, clazz, searchAttribute);
	}

	public abstract BaseRepository getRepository();

	@Override
	public <ID, E extends IdEnabledEntity<ID>> List<E> getAllEntities(
			Class<E> clazz) {
		return getRepository().findAll(clazz);
	}

	@Override
	public <E extends IdEnabledEntity<?>> List<E> getEntities(int startIndex,
			int size, Class<E> clazz) {
		return getRepository().findEntries(startIndex, size, clazz);
	}

	@Override
	public <E extends IdEnabledEntity<?>> long getEntityCount(Class<E> clazz) {
		return getRepository().countEntries(clazz);
	}

	@Override
	public <E extends HasName<?>> List<E> findByName(String name, Class<E> clazz) {
		return getRepository().findByName(name, clazz);
	}
}
