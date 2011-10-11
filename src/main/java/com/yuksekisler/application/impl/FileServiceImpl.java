package com.yuksekisler.application.impl;

import java.util.List;

import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Uploaded;
import com.yuksekisler.domain.UploadedRepository;

public class FileServiceImpl implements FileService {
	private UploadedRepository uploadedRepository;

	@Override
	public <E extends Uploaded> List<E> getFiles(String uploadId, Class<E> clazz) {
		return uploadedRepository.getByUploadId(uploadId, clazz);
	}

	@Override
	public void saveFile(Uploaded uploaded) {
		uploadedRepository.persist(uploaded);
	}

	public void setUploadedRepository(UploadedRepository uploadedRepository) {
		this.uploadedRepository = uploadedRepository;
	}

}
