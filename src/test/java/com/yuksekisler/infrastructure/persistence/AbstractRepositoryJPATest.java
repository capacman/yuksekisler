package com.yuksekisler.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractRepositoryJPATest<R extends BaseRepository, ID, E extends IdEnabledEntity<ID>>
		extends AbstractTransactionalJUnit4SpringContextTests {

	public AbstractRepositoryJPATest() {
		super();
	}

	public int countEnabledRowsInTable(String tableName) {
		return simpleJdbcTemplate.queryForInt("SELECT COUNT(0) FROM "
				+ tableName + " where erased=false");
	}

	@Test
	@Ignore
	public void testPersist() {
		Map<String, Integer> initialTableCountsPersist = getExpectedTableCountsPersist();
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		for (Entry<String, Integer> countEntry : initialTableCountsPersist
				.entrySet()) {
			assertEquals("invalid column count for " + countEntry.getKey(),
					countEntry.getValue().intValue(),
					countEnabledRowsInTable(countEntry.getKey()));
		}
	}

	public abstract Map<String, Integer> getExpectedTableCountsPersist();

	@Test
	@Ignore
	public void testRemove() {
		Map<String, Integer> initialTableCountsPersist = getExpectedTableCountsRemove();
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();

		getRepository().removeEntity(e);
		getRepository().flush();
		for (Entry<String, Integer> countEntry : initialTableCountsPersist
				.entrySet()) {
			assertEquals("invalid column count for " + countEntry.getKey(),
					countEntry.getValue().intValue(),
					countEnabledRowsInTable(countEntry.getKey()));
		}
	}

	public abstract Map<String, Integer> getExpectedTableCountsRemove();

	@Test
	@Ignore
	public void testFindEntries() {
		int initial = countEnabledRowsInTable(getEntityTableName());
		E entity = createEntity();
		getRepository().persist(entity);
		getRepository().flush();

		@SuppressWarnings("unchecked")
		List<E> entries = (List<E>) getRepository().findEntries(0, 10,
				entity.getClass());
		if (initial + 1 >= 10)
			assertEquals(10, entries.size());
		else {
			assertEquals(initial + 1, entries.size());
			assertTrue(entries.contains(entity));
		}
	}

	public abstract String getEntityTableName();

	@SuppressWarnings("unchecked")
	@Test
	@Ignore
	public void testFindEntry() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		assertNotNull(getRepository().getEntity(e.getId(), e.getClass()));
	}

	@Test
	@Ignore
	public void testFindAll() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		assertTrue(getRepository().findAll(e.getClass()).size() > 0);
	}

	@Test
	@Ignore
	public void testCountEntries() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();

		assertTrue(getRepository().countEntries(e.getClass()) > 0);
	}

	@Test
	@Ignore
	public void testSearchEntities() {
		List<E> list = getRepository().query(getSearchQueryObject(),
				getClazz(), getSearchAttribute());
		assertNotNull(list);
		assertEquals(getSearchResultCount(), list.size());
	}

	@Test
	@Ignore
	public void testQueryEntities() {
		List<E> list = getRepository()
				.query(getQueryObject(), getClazz(), null);
		assertNotNull(list);
		assertEquals(getQueryResultCount(), list.size());
	}

	abstract protected int getQueryResultCount();

	abstract protected QueryParameters getQueryObject();

	abstract protected int getSearchResultCount();

	abstract protected String getSearchAttribute();

	abstract protected QueryParameters getSearchQueryObject();

	public abstract E createEntity();

	public abstract Class<E> getClazz();

	protected abstract R getRepository();

}