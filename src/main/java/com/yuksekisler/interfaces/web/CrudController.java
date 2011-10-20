package com.yuksekisler.interfaces.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuksekisler.domain.IdEnabledEntity;

public interface CrudController<ID, E extends IdEnabledEntity<ID>> {
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	E get(@PathVariable("id") ID id);

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	E store(@RequestBody E entity);

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") ID id);

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<E>> query(HttpServletRequest request);
}
