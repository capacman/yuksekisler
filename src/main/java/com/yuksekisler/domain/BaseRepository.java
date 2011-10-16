package com.yuksekisler.domain;

import java.util.List;

public interface BaseRepository {

	public abstract <E extends IdEnabledEntity> E merge(E entity);

	public abstract void clear();

	public abstract void flush();

	public abstract <E extends IdEnabledEntity> void persist(E entity);

	public abstract <E extends IdEnabledEntity> void remove(E entity);

	public abstract <E extends IdEnabledEntity> List<E> findEntries(
			int firstResult, int maxResults, Class<E> clazz);

	public abstract <E extends IdEnabledEntity> E find(Long id, Class<E> clazz);

	public abstract <E extends IdEnabledEntity> List<E> findAll(Class<E> clazz);

	public abstract <E extends IdEnabledEntity> long countEntries(Class<E> clazz);

}