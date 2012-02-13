package com.yuksekisler.web;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuksekisler.application.EquipmentService;
import com.yuksekisler.domain.HasName;
import com.yuksekisler.domain.employee.CertificateType;
import com.yuksekisler.domain.employee.EmployeeTitle;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;

public class DefinitionsController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefinitionsController.class);
	private Category category = new Category();
	private Brand brand = new Brand();
	private CertificateType certificateType = new CertificateType();
	private EmployeeTitle employeeTitle = new EmployeeTitle();
	private EquipmentService equipmentService;
	private SessionInfo sessionInfo;

	public void addCategory() {
		equipmentService.saveEntity(category);
	}

	public void addBrand() {
		equipmentService.saveEntity(brand);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public EquipmentService getEquipmentService() {
		return equipmentService;
	}

	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	public void addCertificateType() {
		this.equipmentService.saveEntity(certificateType);
	}

	public void addEmpoyeeTitle() {
		this.equipmentService.saveEntity(employeeTitle);
	}

	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	public EmployeeTitle getEmployeeTitle() {
		return employeeTitle;
	}

	public void setEmployeeTitle(EmployeeTitle employeeTitle) {
		this.employeeTitle = employeeTitle;
	}

	public void brandNameCheck(FacesContext context, UIComponent component,
			Object value) {
		checkEntityName(value, Brand.class);
	}

	public void categoryNameCheck(FacesContext context, UIComponent component,
			Object value) {
		checkEntityName(value, Category.class);
	}

	public void employeeTitleNameCheck(FacesContext context,
			UIComponent component, Object value) {
		checkEntityName(value, EmployeeTitle.class);
	}

	public void certificateNameCheck(FacesContext context,
			UIComponent component, Object value) {
		checkEntityName(value, CertificateType.class);
	}

	private void checkEntityName(Object value,
			Class<? extends HasName<?>> entityClass) {
		if (checkEntity(entityClass, (String) value)) {
			FacesMessage facesMessage = new FacesMessage(
					sessionInfo.getLocalizedString("alreadyDefined"));
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}
	}

	private boolean checkEntity(Class<? extends HasName<?>> clazz, String name) {
		List<? extends HasName<?>> allEntities = equipmentService
				.getAllEntities(clazz);
		return doubleNameCheck(name, allEntities);
	}

	private boolean doubleNameCheck(String name,
			List<? extends HasName<?>> entities) {
		for (HasName<?> hasName : entities) {
			if (name.equalsIgnoreCase(hasName.getName()))
				return true;
		}
		return false;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

}
