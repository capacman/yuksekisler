package com.yuksekisler.interfaces.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.yuksekisler.domain.IdEnabledEntity;

public interface CrudController<ID, E extends IdEnabledEntity<ID>> {
	public E get(ID id);

	E store(E entity);

	public void delete(ID id);

	public ResponseEntity<List<E>> query(HttpServletRequest request);
}
