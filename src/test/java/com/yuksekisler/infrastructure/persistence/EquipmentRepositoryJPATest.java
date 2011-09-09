package com.yuksekisler.infrastructure.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/*Context.xml")
public class EquipmentRepositoryJPATest extends
		AbstractRepositoryJPATest<EquipmentRepository, Equipment> {
	@Autowired
	EquipmentRepository equipmentRepository;

	@Override
	public Map<String, Integer> getExpectedTableCountsPersist() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EQUIPMENT", countRowsInTable("EQUIPMENT") + 1);
		counts.put("BRAND", countRowsInTable("BRAND") + 1);
		counts.put("CATEGORY", countRowsInTable("CATEGORY") + 1);
		return counts;
	}

	@Override
	public Map<String, Integer> getExpectedTableCountsRemove() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EQUIPMENT", countRowsInTable("EQUIPMENT"));
		return counts;
	}

	@Override
	public Equipment createEntity() {
		Equipment equipment = new Equipment();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 10);
		equipment.setBestBeforeDate(calendar.getTime());
		equipment.setProductCode("productCode");
		equipment.setProductionDate(new Date());
		equipment.setProductName("productName");
		calendar.add(Calendar.DAY_OF_MONTH, -20);
		equipment.setStockEntrance(calendar.getTime());
		Category category = new Category("foo", "bar");
		equipmentRepository.persist(category);
		Brand brand = new Brand("foo", "bar");
		equipmentRepository.persist(brand);
		equipmentRepository.flush();

		equipment.setBrand(brand);
		equipment.setCategory(category);
		return equipment;
	}

	@Override
	protected EquipmentRepository getRepository() {
		return equipmentRepository;
	}

	@Override
	public String getEntityTableName() {
		return "EQUIPMENT";
	}

}
