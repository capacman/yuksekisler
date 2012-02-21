package com.yuksekisler.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.component.html.HtmlInputHidden;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.web.mvc.FileController.ImageSaveFailedException;

public class EquipmentFormController extends AbstractEquipmentController {
	private static final Logger LOGGER = org.slf4j.LoggerFactory
			.getLogger(EquipmentFormController.class);
	private SessionInfo sessionInfo;

	private FileService fileService;
	private InspectionReport inspectionReport = new InspectionReport();
	private String uuid = UUID.randomUUID().toString();
	private HtmlInputHidden htmlInputText;

	// since equipment value decides whether some views should be visible or not
	// it should be not null

	public void setEquipmentService(final EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String create() {
		List<Image> files = fileService.getFiles(uuid, Image.class);
		for (Image image : files) {
			equipment.addImage(image);
		}
		equipmentService.saveEntity(equipment);
		return "equipment";
	}

	public String update() {
		LOGGER.info("inside update");
		equipmentService.saveEntity(equipment);
		return "view";
	}

	public String cancelUpdate() {
		return "view";
	}

	public String cancel() {
		return "equipment";
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
			image.setUploadId(uuid);
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

	public String createInspectionReport() {
		inspectionReport.setInspector(sessionInfo.getUser());
		equipment.addInspectionReport(inspectionReport);
		equipmentService.saveEntity(equipment);
		return "view";
	}

	public String cancelInspectionReport() {
		equipmentId = (Long) htmlInputText.getValue();
		return "view";
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
