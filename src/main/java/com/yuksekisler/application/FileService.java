package com.yuksekisler.application;

import java.util.List;

import com.yuksekisler.domain.Uploaded;

public interface FileService {

	void saveFile(Uploaded uploaded);

	<E extends Uploaded> List<E> getFiles(String uploadId, Class<E> clazz);

	<E extends Uploaded> E getFile(Long id, Class<E> clazz);
}
