package com.yuksekisler.interfaces.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

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
	List<Map<String, ?>> update(@RequestParam("uploadId") String uploadId,
			@RequestParam("uploadedfiles[]") MultipartFile[] files) {

		LOGGER.debug("number of files to upload {}", files.length);
		List<Map<String, ?>> returnValues = new ArrayList<Map<String, ?>>();
		for (MultipartFile multipartFile : files) {
			Map<String, Object> builder = new HashMap<String, Object>();
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
				fileService.saveEntity(image);
				builder.put("file", multipartFile.getOriginalFilename());
				builder.put("name", multipartFile.getOriginalFilename());
				builder.put("width", width);
				builder.put("height", height);
				builder.put("type",
						multipartFile.getContentType().split("/")[1]);
				builder.put("result", "success");
				returnValues.add(builder);
			} catch (IOException e) {
				builder.put("file", multipartFile.getOriginalFilename());
				builder.put("name", multipartFile.getOriginalFilename());
				builder.put("type",
						multipartFile.getContentType().split("/")[1]);
				builder.put("result", "success");
				returnValues.add(builder);
			} catch (Exception e) {
				throw new ImageSaveFailedException(e);
			}

		}
		return returnValues;
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
	public void getImage(@PathVariable("id") Long id,
			HttpServletResponse response) throws IOException {
		Image image = fileService.getEntity(id, Image.class);
		response.setContentType(image.getMimeType());
		response.setContentLength(image.getImageData().length);
		OutputStream out = response.getOutputStream();
		out.write(image.getImageData());
		out.close();
	}

	@RequestMapping(value = "/image/{id}/thumbnail", method = RequestMethod.GET)
	public void getImageThumbnail(@PathVariable("id") Long id,
			HttpServletResponse response) throws IOException {
		Image image = fileService.getEntity(id, Image.class);
		response.setContentType("image/png");
		response.setContentLength(image.getThumbnailData().length);
		OutputStream out = response.getOutputStream();
		out.write(image.getThumbnailData());
		out.close();
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(ImageSaveFailedException.class)
	public Map<String, String> handleImageSaveFailed() {
		// we should return ok and send client error in other form
		Map<String, String> errorResponse = new HashMap<String, String>();
		errorResponse.put("operationFailed", "true");
		return errorResponse;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public static class ImageSaveFailedException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6562474708165200842L;

		public ImageSaveFailedException() {
			super();
		}

		public ImageSaveFailedException(String message, Throwable cause) {
			super(message, cause);
		}

		public ImageSaveFailedException(String message) {
			super(message);
		}

		public ImageSaveFailedException(Throwable cause) {
			super(cause);
		}

	}
}
