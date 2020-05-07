package org.chi.dpuk.sensing.platform.participant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.utils.ModelObjectCreationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ParticipantServiceImplTest {

	@Autowired
	private ParticipantService participantService;

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
	@ExpectedDatabase(value = "participantServiceImpl-addParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addParticipant() throws Exception {
		participantService.addOrUpdateParticipant(ModelObjectCreationHelper.createTestParticipant());
	}

	@Test
	@DatabaseSetup("participantServiceImpl-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantServiceImpl-updateParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void updateParticipant() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("updated 01132456789");
		participant.setNhsNumber("1234567890");

		participantService.addOrUpdateParticipant(participant);
	}

	@Test
	@DatabaseSetup("participantServiceImpl-addDataSourceToParticipant-setup.xml")
	@ExpectedDatabase(value = "participantServiceImpl-addDataSourceToParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addDataSourceToParticipant() throws Exception {
		Participant participant = participantService.addDataSourceToParticipant(1100l, 1050l);
		assertNotNull(participant);
		Set<DataSource> dataSources = participant.getDataSources();
		assertTrue(dataSources.size() == 1);
		for (DataSource dataSource : dataSources) {
			assertTrue(dataSource.getId() == 1100l);
			assertEquals(dataSource.getName(), "DataSource Name 1");
			assertEquals(dataSource.getDescription(), "DataSource Description 1");
			assertEquals(dataSource.getType(), "DataSource Type 1");
			assertEquals(dataSource.getUserName(), "DataSourceUserName1");
			assertEquals(dataSource.getPassword(), "DataSource Password 1");
			assertTrue(dataSource.getParticipant().getId() == 1050l);
		}
		assertTrue(participant.getId() == 1050l);
		assertEquals(participant.getName(), "one name");
		assertEquals(participant.getAddress(), "one address");
		assertEquals(participant.getContactNumber(), "01132456789");
	}

	@Test
	@DatabaseSetup("participantServiceImpl-addStudyToParticipant-setup.xml")
	@ExpectedDatabase(value = "participantServiceImpl-addStudyToParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addStudyToParticipant() throws Exception {
		Participant participant = participantService.addStudyToParticipant(1000l, 1050l);
		assertNotNull(participant);
		Study study = participant.getStudy();
		assertNotNull(study);
		assertTrue(study.getId() == 1000l);
		assertEquals(study.getName(), "Study Name 1");
		assertEquals(study.getDescription(), "Study Description");
		assertTrue(participant.getId() == 1050l);
		assertEquals(participant.getName(), "one name");
		assertEquals(participant.getAddress(), "one address");
		assertEquals(participant.getContactNumber(), "01132456789");
	}

}