package com.yuksekisler.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import bsh.This;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.web.mvc.FileController.ImageSaveFailedException;

public class EquipmentController {
	private static final Logger LOGGER = org.slf4j.LoggerFactory
			.getLogger(EquipmentController.class);
	private EquipmentService equipmentService;
	private SessionInfo sessionInfo;
	private LazyDataModel<Equipment> model;
	private Equipment equipment = new Equipment();
	private Long equipmentId;
	private String uuid = "";
	private String newuuid = "";
	private FileService fileService;

	// since equipment value decides whether some views should be visible or not
	// it should be not null

	public void setEquipmentService(final EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
		model = new LazyDataModel<Equipment>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1961966247543272506L;

			@Override
			public List<Equipment> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				return equipmentService.getEntities(first, pageSize,
						Equipment.class);
			}

		};
		model.setRowCount((int) equipmentService
				.getEntityCount(Equipment.class));
		model.setPageSize(10);
	}

	public LazyDataModel<Equipment> getModel() {
		return model;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public List<Category> getCategories() {
		return equipmentService.getAllEntities(Category.class);
	}

	public List<Brand> getBrands() {
		return equipmentService.getAllEntities(Brand.class);
	}

	public String create() {
		LOGGER.info("in create newuuid {}", newuuid);
		String[] imageSplits = newuuid.split(";");
		for (String imageUUID : imageSplits) {
			if (!imageUUID.isEmpty()) {
				List<Image> files = fileService
						.getFiles(imageUUID, Image.class);
				for (Image image : files) {
					equipment.addImage(image);
				}
			}
		}
		equipmentService.saveEntity(equipment);
		return "equipment";
	}

	public String cancel() {
		return "equipment";
	}

	public void loadEquipment() {
		if (equipmentId != null) {
			try {
				equipment = equipmentService.getEntity(equipmentId,
						Equipment.class);
				LOGGER.debug("equipment find {}", equipment);
			} catch (Exception e) {
				equipment = null;
			}
		} else {
			LOGGER.info("equipment id is null");
		}
		if (equipment != null) {
			LOGGER.debug("equipment find not null");
			LOGGER.debug("equipment has {} inspection reports", equipment
					.getInspectionReports().size());
		}
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public void handleFileUpload(FileUploadEvent fileUploadEvent) {

		LOGGER.info("file upload detected");
		try {
			Image image = new Image();
			image.setTitle(fileUploadEvent.getFile().getFileName());
			image.setMimeType(fileUploadEvent.getFile().getContentType());
			image.setImageData(IOUtils.toByteArray(fileUploadEvent.getFile()
					.getInputstream()));
			ByteArraySeekableStream seekableStream = new ByteArraySeekableStream(
					image.getImageData());
			RenderedOp renderedOp = JAI.create("stream", seekableStream);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BufferedImage bufferedImage = renderedOp.getAsBufferedImage();
			Thumbnails.of(bufferedImage).size(100, 100).outputFormat("png")
					.toOutputStream(outputStream);
			image.setThumbnailData(outputStream.toByteArray());
			String uuid = UUID.randomUUID().toString();
			newuuid += uuid + ";";
			LOGGER.info("new uuid {}", newuuid);
			image.setUploadId(uuid);
			fileService.saveEntity(image);
		} catch (IOException e) {
			throw new ImageSaveFailedException(e);
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = newuuid + uuid;
		LOGGER.info("set uuid called {}", this.uuid);
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

}
