package com.yuksekisler.web.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yuksekisler.application.FileService;
import com.yuksekisler.domain.Image;

@RequestMapping("/")
public class FileController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileController.class);
	// private CommonsMultipartResolver resolver;
	private FileService fileService;

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
