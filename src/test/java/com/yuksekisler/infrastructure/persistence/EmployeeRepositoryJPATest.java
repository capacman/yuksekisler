package com.yuksekisler.infrastructure.persistence;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.employee.EmployeeIdentity;
import com.yuksekisler.domain.employee.EmployeeRepository;
import com.yuksekisler.domain.employee.EmployeeTitle;
import com.yuksekisler.domain.employee.IdentityType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/*Context.xml")
public class EmployeeRepositoryJPATest extends
		AbstractRepositoryJPATest<EmployeeRepository, Long, Employee> {

	@Override
	public Employee createEntity() {
		Employee e = new Employee();
		e.setName("foo");
		e.setEmail("foo@bar.com");
		e.setPassword("secret");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		e.setStartDate(calendar.getTime());
		e.setEmployeeIdentity(new EmployeeIdentity("123456", IdentityType.VKN));

		EmployeeTitle title = new EmployeeTitle("title", "titleDescription");
		getRepository().persist(title);
		getRepository().flush();

		e.setTitle(title);
		return e;
	}

	@Override
	public Map<String, Integer> getExpectedTableCountsPersist() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EMPLOYEE", countEnabledRowsInTable("EMPLOYEE") + 1);
		counts.put("EMPLOYEE_TITLE",
				countEnabledRowsInTable("EMPLOYEE_TITLE") + 1);
		return counts;
	}

	@Override
	public Map<String, Integer> getExpectedTableCountsRemove() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("EMPLOYEE", countEnabledRowsInTable("EMPLOYEE"));
		return counts;
	}

	@Autowired
	private EmployeeRepository repository;

	@Override
	protected EmployeeRepository getRepository() {
		return repository;
	}

	@Override
	public String getEntityTableName() {
		return "EMPLOYEE";
	}

	@Override
	protected String getSearchAttribute() {
		return "name";
	}

	@Override
	protected QueryParameters getSearchQueryObject() {
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.setOrder("name", true);
		queryParameters.setSearchString("nil");
		return queryParameters;
	}

	@Override
	public Class<Employee> getClazz() {
		return Employee.class;
	}

	@Override
	protected int getSearchResultCount() {
		return 1;
	}

	@Override
	protected QueryParameters getQueryObject() {
		QueryParameters queryParameters = new QueryParameters();
		queryParameters.setOrder("name", true);
		queryParameters.addParameter("title.name", "foo1");
		return queryParameters;
	}

	@Override
	protected int getQueryResultCount() {
		return 1;
	}

}
