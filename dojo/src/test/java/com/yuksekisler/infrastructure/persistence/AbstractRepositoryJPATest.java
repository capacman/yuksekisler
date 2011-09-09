package com.yuksekisler.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.IdEnabledEntity;

public abstract class AbstractRepositoryJPATest<R extends BaseRepository, E extends IdEnabledEntity>
		extends AbstractTransactionalJUnit4SpringContextTests {

	public AbstractRepositoryJPATest() {
		super();
	}

	@Test
	public void testPersist() {
		Map<String, Integer> initialTableCountsPersist = getExpectedTableCountsPersist();
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		for (Entry<String, Integer> countEntry : initialTableCountsPersist
				.entrySet()) {
			assertEquals(countEntry.getValue().intValue(),
					countRowsInTable(countEntry.getKey()));
		}
	}

	public abstract Map<String, Integer> getExpectedTableCountsPersist();

	@Test
	public void testRemove() {
		Map<String, Integer> initialTableCountsPersist = getExpectedTableCountsRemove();
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();

		getRepository().remove(e);
		getRepository().flush();
		for (Entry<String, Integer> countEntry : initialTableCountsPersist
				.entrySet()) {
			assertEquals(countEntry.getValue().intValue(),
					countRowsInTable(countEntry.getKey()));
		}
	}

	public abstract Map<String, Integer> getExpectedTableCountsRemove();

	@Test
	public void testFindEntries() {
		int initial = countRowsInTable(getEntityTableName());
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

	@Test
	public void testFindEntry() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		assertNotNull(getRepository().find(e.getId(), e.getClass()));
	}

	@Test
	public void testFindAll() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();
		assertTrue(getRepository().findAll(e.getClass()).size() > 0);
	}

	@Test
	public void testCountEntries() {
		E e = createEntity();
		getRepository().persist(e);
		getRepository().flush();

		assertTrue(getRepository().countEntries(e.getClass()) > 0);
	}

	public abstract E createEntity();

	protected abstract R getRepository();

}