package com.yuksekisler.interfaces.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Image;

@RequestMapping("/file")
public class FileController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileController.class);
	// private CommonsMultipartResolver resolver;
	private FileService fileService;

	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public @ResponseBody
	List<String> update(@RequestParam("uploadId") String uploadId,
			@RequestParam("uploadedfiles[]") MultipartFile[] files) {

		LOGGER.debug("number of files to upload {}", files.length);
		List<String> returnValues = new ArrayList<String>();
		for (MultipartFile multipartFile : files) {
			try {
				Image image = new Image();
				image.setTitle(multipartFile.getOriginalFilename());
				image.setMimeType(multipartFile.getContentType());
				image.setImageData(multipartFile.getBytes());
				ByteArraySeekableStream seekableStream = new ByteArraySeekableStream(
						image.getImageData());
				RenderedOp renderedOp = JAI.create("stream", seekableStream);

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				BufferedImage bufferedImage = renderedOp.getAsBufferedImage();
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				Thumbnails.of(bufferedImage).size(100, 100).outputFormat("png")
						.toOutputStream(outputStream);
				image.setThumbnailData(outputStream.toByteArray());
				image.setUploadId(uploadId);
				fileService.saveFile(image);
				StringBuilder builder = new StringBuilder();
				builder.append("file=")
						.append(multipartFile.getOriginalFilename())
						.append(",name=")
						.append(multipartFile.getOriginalFilename())
						.append(",width=").append(width).append(",height=")
						.append(height).append(",type=")
						.append(multipartFile.getContentType().split("/")[1]);
				returnValues.add(builder.toString());
			} catch (IOException e) {
				LOGGER.error("equipment image saving failed", e);
				// TODO: handle exception and notify user
			}
		}
		return returnValues;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
}
