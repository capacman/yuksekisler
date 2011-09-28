package com.yuksekisler.interfaces.web;

import java.awt.image.BufferedImage;
import java.beans.PropertyEditorSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.equipment.Equipment;

@RequestMapping("/equipment")
public class EquipmentController extends AbstractBaseController {
	static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentController.class);
	private EquipmentService equipmentService;

	@InitBinder({ "stockEntrance", "bestBeforeDate", "productionDate" })
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					LOGGER.debug("starting to parse value: {}", value);
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					LOGGER.warn("web data binder failed for value: {}", value);
					setValue(null);
				}
			}

			public String getAsText() {
				return new SimpleDateFormat("dd/MM/yyyy")
						.format((Date) getValue());
			}

		});

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Equipment get(@PathVariable("id") Long id) {
		return equipmentService.getEquipment(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public @ResponseBody
	List<String> store(
			@RequestParam("productName") String productName,
			@RequestParam("productCode") String productCode,
			@RequestParam("category") Long categoryId,
			@RequestParam("brand") Long brandId,
			@RequestParam("stockEntrance") Date stockEntrance,
			@RequestParam("bestBeforeDate") Date bestBeforeDate,
			@RequestParam("productionDate") Date productionDate,
			@RequestParam(value = "uploadedfiles[]", required = false) MultipartFile[] files) {
		Equipment equipment = new Equipment();
		List<String> returnValues = saveOrUpdateEquipment(productName,
				productCode, categoryId, brandId, stockEntrance,
				bestBeforeDate, productionDate, files, equipment);
		equipmentService.saveEquipment(equipment);
		return returnValues;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public @ResponseBody
	List<String> update(
			@PathVariable("id") Long equipmentId,
			@RequestParam("productName") String productName,
			@RequestParam("productCode") String productCode,
			@RequestParam("category") Long categoryId,
			@RequestParam("brand") Long brandId,
			@RequestParam("stockEntrance") Date stockEntrance,
			@RequestParam("bestBeforeDate") Date bestBeforeDate,
			@RequestParam("productionDate") Date productionDate,
			@RequestParam(value = "uploadedfiles[]", required = false) MultipartFile[] files) {
		Equipment equipment = equipmentService.getEquipment(equipmentId);
		List<String> returnValues = saveOrUpdateEquipment(productName,
				productCode, categoryId, brandId, stockEntrance,
				bestBeforeDate, productionDate, files, equipment);
		equipmentService.saveEquipment(equipment);
		return returnValues;
	}

	protected List<String> saveOrUpdateEquipment(String productName,
			String productCode, Long categoryId, Long brandId,
			Date stockEntrance, Date bestBeforeDate, Date productionDate,
			MultipartFile[] files, Equipment equipment) {
		equipment.setProductCode(productCode);
		equipment.setProductName(productName);
		equipment.setProductionDate(productionDate);
		equipment.setStockEntrance(stockEntrance);
		equipment.setBestBeforeDate(bestBeforeDate);
		equipment.setCategory(equipmentService.getCategory(categoryId));
		equipment.setBrand(equipmentService.getBrand(brandId));
		LOGGER.debug("store equipment {}", equipment);
		List<String> returnValues = new ArrayList<String>();
		if (files != null && files.length != 0) {
			LOGGER.debug("there are some files");
			for (MultipartFile multipartFile : files) {
				try {
					Image image = new Image();
					image.setTitle(multipartFile.getOriginalFilename());
					image.setMimeType(multipartFile.getContentType());
					image.setImageData(multipartFile.getBytes());
					equipment.addImage(image);
					ByteArraySeekableStream seekableStream = new ByteArraySeekableStream(
							image.getImageData());
					RenderedOp renderedOp = JAI
							.create("stream", seekableStream);

					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					BufferedImage bufferedImage = renderedOp
							.getAsBufferedImage();
					int width = bufferedImage.getWidth();
					int height = bufferedImage.getHeight();
					Thumbnails.of(bufferedImage).size(100, 100)
							.outputFormat("png").toOutputStream(outputStream);
					image.setThumbnailData(outputStream.toByteArray());
					StringBuilder builder = new StringBuilder();
					builder.append("file=")
							.append(multipartFile.getOriginalFilename())
							.append(",name=")
							.append(multipartFile.getOriginalFilename())
							.append(",width=")
							.append(width)
							.append(",height=")
							.append(height)
							.append(",type=")
							.append(multipartFile.getContentType().split("/")[1]);
					returnValues.add(builder.toString());
				} catch (IOException e) {
					LOGGER.error("equipment image saving failed", e);
					// TODO: handle exception and notify user
				}
			}
		} else
			LOGGER.debug("there is no files");
		return returnValues;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		equipmentService.removeEquipment(equipmentService.getEquipment(id));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Equipment>> query(HttpServletRequest request,
			HttpServletResponse response) {
		QueryParameters parameters = prepareQueryParameters(request);
		LOGGER.debug(parameters.toString());
		if ((parameters.getSearchString() != null && !parameters
				.getSearchString().isEmpty())
				|| parameters.hasRange()
				|| parameters.hasOrder()) {
			List<Equipment> queryResult = equipmentService
					.queryEquipment(parameters);
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.add(
					"Content-Range",
					"items " + parameters.getRangeStart() + "-"
							+ (parameters.getRangeStart()+queryResult.size()<parameters.getRangeEnd()?parameters.getRangeStart()+queryResult.size():parameters.getRangeEnd()) + "/"
							+ equipmentService.getEquipmentCount());
			ResponseEntity<List<Equipment>> responseEntity = new ResponseEntity<List<Equipment>>(
					queryResult, headers, HttpStatus.OK);
			return responseEntity;
		} else {
			return new ResponseEntity<List<Equipment>>(
					equipmentService.getEquipments(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
	public void getImage(@PathVariable("id") Long id,
			HttpServletResponse response) throws IOException {
		Image image = equipmentService.getImage(id);
		response.setContentType(image.getMimeType());
		response.setContentLength(image.getImageData().length);
		OutputStream out = response.getOutputStream();
		out.write(image.getImageData());
		out.close();
	}

	@RequestMapping(value = "/image/{id}/thumbnail", method = RequestMethod.GET)
	public void getImageThumbnail(@PathVariable("id") Long id,
			HttpServletResponse response) throws IOException {
		Image image = equipmentService.getImage(id);
		response.setContentType("image/png");
		response.setContentLength(image.getThumbnailData().length);
		OutputStream out = response.getOutputStream();
		out.write(image.getThumbnailData());
		out.close();
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
