package org.chi.dpuk.sensing.platform.dataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.utils.ModelObjectCreationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/*
 * As we are using Spring, we use the Spring unit test runner
 * To get spring to do the autowiring for us, we pass it the necessary xml files in the ContextConfiguration annotation
 * The TestExecutionListeners specified here are necessary for the DBUnit / Spring integration to work correctly
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class DataSourceServiceImplTest {

	@Autowired
	private DataSourceService dataSourceService;

	/*
	 * Prior to running the test, DBUnit will initialise the databases as
	 * specified in the xml file passed to the DatabaseSetup annotation. After
	 * running the test, it will compare the results with the value attribute
	 * passed to the ExpectedDatabase annotation. By setting the database
	 * assertion mode to "NON_STRICT" we tell DBUnit to ignore any tables it
	 * finds in the database that are not in the "expected" xml file. If we
	 * don't do this, it will try to find a match for ALL tables in the
	 * database. When testing a specific area of functionality we usually want
	 * to test a subset of the tables.
	 */

	@Test
	@DatabaseSetup("service-empty.xml")
	@ExpectedDatabase(value = "dataSourceServiceImpl-addDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addDataSource() throws Exception {
		dataSourceService.addOrUpdateDataSource(ModelObjectCreationHelper.createTestDataSource());
	}

	@Test
	@DatabaseSetup("dataSourceServiceImpl-updateDataSource-setup.xml")
	@ExpectedDatabase(value = "dataSourceServiceImpl-updateDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void updateDataSource() throws Exception {
		DataSource dataSource = dataSourceService.getDataSource(1);

		dataSource.setDescription("Updated Description");
		dataSource.setName("Updated Name");
		dataSource.setPassword("Updated Password");
		dataSource.setType("Updated Type");
		dataSource.setUserName("UpdatedUserName");

		dataSourceService.addOrUpdateDataSource(dataSource);
	}

	@Test
	@DatabaseSetup("dataSourceServiceImpl-addParticipantToDataSource-setup.xml")
	@ExpectedDatabase(value = "dataSourceServiceImpl-addParticipantToDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addParticipantToDataSource() throws Exception {
		DataSource dataSource = dataSourceService.addParticipantToDataSource(1050l, 1100l);
		assertNotNull(dataSource);
		Participant participant = dataSource.getParticipant();
		assertNotNull(participant);
		assertTrue(participant.getId() == 1050l);
		assertEquals(participant.getName(), "one name");
		assertEquals(participant.getAddress(), "one address");
		assertEquals(participant.getContactNumber(), "01132456789");
		assertTrue(dataSource.getId() == 1100l);
		assertEquals(dataSource.getName(), "DataSource Name 1");
		assertEquals(dataSource.getDescription(), "DataSource Description 1");
		assertEquals(dataSource.getType(), "DataSource Type 1");
		assertEquals(dataSource.getUserName(), "DataSourceUserName1");
		assertEquals(dataSource.getPassword(), "DataSource Password 1");
	}

	@Test
	@DatabaseSetup("dataSourceServiceImpl-addDataSetToDataSource-setup.xml")
	@ExpectedDatabase(value = "dataSourceServiceImpl-addDataSetToDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addDataSetToDataSource() throws Exception {
		final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		DataSet newDataSet = ModelObjectCreationHelper.createTestDataSet();
		DataSource dataSource = dataSourceService.addDataSetToDataSource(newDataSet, 1000l);
		assertNotNull(dataSource);
		assertTrue(dataSource.getDataSets().size() == 1);
		for (DataSet dataSet : dataSource.getDataSets()) {
			assertNotNull(dataSet);
			assertNotNull(dataSet.getPayload());
			assertTrue(dataSet.getPayloadSize() > 0);
			assertEquals(simpleDateFormat.format(dataSet.getStartDate()), "2015-10-15 14:15:00.123");
			assertEquals(simpleDateFormat.format(dataSet.getEndDate()), "2015-10-15 15:15:00.456");
			assertEquals(dataSet.getPayloadContentType(), "Payload Content Type 1");
			assertEquals(dataSet.getPayloadNameWithExtension(), "Payload Name With Extension 1");
		}
		assertTrue(dataSource.getId() == 1000l);
		assertEquals(dataSource.getName(), "DataSource Name 1");
		assertEquals(dataSource.getDescription(), "DataSource Description 1");
		assertEquals(dataSource.getType(), "DataSource Type 1");
		assertEquals(dataSource.getUserName(), "DataSourceUserName1");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		encoder.matches("DataSource Password 1", dataSource.getPassword());
	}

}