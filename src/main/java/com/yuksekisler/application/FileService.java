package com.yuksekisler.application;

import java.util.List;

import com.yuksekisler.domain.Uploaded;

public interface FileService extends CrudService {

	<E extends Uploaded> List<E> getFiles(String uploadId, Class<E> clazz);

}
