package com.yuksekisler.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.interfaces.web.AbstractBaseCrudController;

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
		counts.put("BRAND", countEnabledRowsInTable("BRAND") + 1);
		counts.put("CATEGORY", countEnabledRowsInTable("CATEGORY") + 1);
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

	@Test
	public void testRegEx() {
		Matcher matcher = AbstractBaseCrudController.SORT_PATTERN
				.matcher("sort( osman)");
		assertTrue(matcher.matches());
		String group1 = matcher.group(1);
		assertEquals(1, group1.length());
		String group2 = matcher.group(2);
		assertEquals(5, group2.length());

	}

	@Test
	public void testfindByName() {
		assertFalse(equipmentRepository.findByName("a", Category.class)
				.isEmpty());
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
