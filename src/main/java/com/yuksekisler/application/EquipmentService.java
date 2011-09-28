package com.yuksekisler.application;

import java.util.Date;
import java.util.List;

import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;

public interface EquipmentService {
	Equipment createEquipment(Category category, Brand brand,
			Date stockEntranceDate, Date bestBefore, Date productionDate,
			String productCode, String productName);

	Equipment saveEquipment(Equipment equipment);

	List<Equipment> getEquipments(int startIndex, int size);

	List<Equipment> getEquipments();

	long getEquipmentCount();

	Brand createNewBrand(Brand brand);

	Category createNewCategory(Category category);

	List<Brand> getBrands();

	List<Category> getCategories();

	Brand getBrand(Long id);

	Category getCategory(Long id);

	Equipment getEquipment(Long id);

	InspectionReport saveInspectionReport(Long reportEquipmentId,
			InspectionReport report);

	void removeEquipment(Equipment equipment);

	void removeCategory(Category category);

	void removeBrand(Brand brand);

	List<Equipment> queryEquipment(QueryParameters queryParameters);

	Image getImage(Long id);
}
