package com.yuksekisler.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputHidden;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;

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
	private FileService fileService;
	private FileUploadFormUUIDBean uuidBean;
	private InspectionReport inspectionReport = new InspectionReport();
	private HtmlInputHidden htmlInputText;

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
		String[] imageSplits = uuidBean.getUuid().split(";");
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

	public String update() {
		equipmentService.saveEntity(equipment);
		return "view";
	}

	public String cancelUpdate() {
		return "cancel";
	}

	public String cancel() {
		return "equipment";
	}

	public void loadEquipment() {
		if (equipmentId != null) {
			LOGGER.info("equipment id is {}", equipmentId);
			try {
				equipment = equipmentService.getEntity(equipmentId,
						Equipment.class);
			} catch (Exception e) {
				equipment = null;
			}
		} else {
			LOGGER.info("equipment id is null");
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
			LOGGER.info("new uuid {}", uuidBean.getUuid());
			image.setUploadId(uuidBean.getUuid());
			fileService.saveEntity(image);
		} catch (IOException e) {
			throw new ImageSaveFailedException(e);
		}
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public FileUploadFormUUIDBean getUuidBean() {
		return uuidBean;
	}

	public void setUuidBean(FileUploadFormUUIDBean uuidBean) {
		this.uuidBean = uuidBean;
	}

	public List<Long> getImageIds() {
		List<Long> imageIDs = new ArrayList<Long>();
		for (Image img : equipment.getImages()) {
			imageIDs.add(img.getId());
		}
		return imageIDs;
	}

	public InspectionReport getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(InspectionReport inspectionReport) {
		this.inspectionReport = inspectionReport;
	}

	public String addInspectionReport() {
		return "addInspectionReport";
	}

	public String createInspectionReport() {
		loadEquipment();
		inspectionReport.setInspector(sessionInfo.getUser());
		equipment.addInspectionReport(inspectionReport);
		equipmentService.saveEntity(equipment);
		return "view";
	}

	public String cancelInspectionReport() {
		equipmentId = (Long) htmlInputText.getValue();
		return "view";
	}

	public HtmlInputHidden getHtmlInputText() {
		return htmlInputText;
	}

	public void setHtmlInputText(HtmlInputHidden htmlInputText) {
		this.htmlInputText = htmlInputText;
	}

	public void handleImageClose(CloseEvent event) {
		LOGGER.info("image id {} ",
				event.getComponent().getAttributes().get("imageID"));
		long imageID = (Long) event.getComponent().getAttributes()
				.get("imageID");
		Image image = equipmentService.getEntity(imageID, Image.class);
		image.setErased(true);
		equipmentService.saveEntity(image);
	}
}
