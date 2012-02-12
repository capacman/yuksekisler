package com.yuksekisler.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.equipment.InspectionStatus;
import com.yuksekisler.domain.work.LifeTime;
import com.yuksekisler.domain.work.WorkDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/*Context.xml")
public class EquipmentRepositoryJPATest extends
		AbstractRepositoryJPATest<EquipmentRepository, Long, Equipment> {
	@Autowired
	EquipmentRepository equipmentRepository;

	@Override
	public Map<String, Integer> getExpectedTableCountsPersist() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EQUIPMENT", countEnabledRowsInTable("EQUIPMENT") + 1);
		counts.put("BRAND", countEnabledRowsInTable("BRAND"));
		counts.put("CATEGORY", countEnabledRowsInTable("CATEGORY"));
		return counts;
	}

	@Override
	public Map<String, Integer> getExpectedTableCountsRemove() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EQUIPMENT", countEnabledRowsInTable("EQUIPMENT"));
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
		Category category = equipmentRepository.getEntity(1L, Category.class);

		Brand brand = equipmentRepository.getEntity(1L, Brand.class);

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

	@Test
	public void testfindByName() {
		assertFalse(equipmentRepository.findByName("a", Category.class)
				.isEmpty());
	}

	@Test
	public void testByInspectionReport() {
		Equipment createEntity = createEntity();
		equipmentRepository.saveEntity(createEntity);
		equipmentRepository.flush();

		InspectionReport report = new InspectionReport(
				equipmentRepository.getEntity(1l, Employee.class), new Date(),
				"ok", InspectionStatus.NOTUSABLE);

		createEntity.addInspectionReport(report);
		equipmentRepository.persist(createEntity);

		assertEquals(createEntity,
				equipmentRepository.findByInspectionReport(report));
	}

	@Test
	public void testGetAvailable() {
		WorkDefinition workDefinition = new WorkDefinition();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.DAY_OF_MONTH, -1);
		workDefinition.setLifeTime(new LifeTime(instance.getTime(), null));
		workDefinition.setCustomer("customer");
		workDefinition.setName("workName");

		Equipment equipment = equipmentRepository.saveEntity(createEntity());
		equipmentRepository.flush();
		Equipment equipment2 = equipmentRepository.saveEntity(createEntity());
		equipmentRepository.flush();
		Employee employee = equipmentRepository.getEntity(1l, Employee.class);

		workDefinition.addSupervisor(employee);
		workDefinition.addEquipment(equipment);

		equipmentRepository.saveEntity(workDefinition);
		equipmentRepository.flush();
		instance.add(Calendar.DAY_OF_MONTH, 2);
		List<Equipment> availableList = equipmentRepository.findAvailable(
				new LifeTime(new Date(), instance.getTime()), null,
				workDefinition.getId());
		for (Equipment availableEquipment : availableList) {
			assertFalse(equipment.getId() == availableEquipment.getId());
		}
		assertTrue(availableList.size() >= 1);
		assertTrue(availableList.contains(equipment2));

	}

	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testHasNameUnique() {
		equipmentRepository.saveEntity(new Brand("a", "a"));
	}

	@Override
	protected int getSearchResultCount() {
		return 11;
	}

	@Override
	protected String getSearchAttribute() {
		return "productName";
	}

	@Override
	protected QueryParameters getSearchQueryObject() {
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.setOrder("productName", true);
		queryParameters.setSearchString("product1");
		return queryParameters;
	}

	@Override
	public Class<Equipment> getClazz() {
		return Equipment.class;
	}

	@Override
	protected int getQueryResultCount() {
		return 30;
	}

	@Override
	protected QueryParameters getQueryObject() {
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.setOrder("productName", true);
		queryParameters.addParameter("brand.name", "a");
		return queryParameters;
	}
}
