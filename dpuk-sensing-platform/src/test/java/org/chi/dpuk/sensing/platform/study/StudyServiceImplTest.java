package org.chi.dpuk.sensing.platform.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
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
// TODO:- Remove  the unused xml "studyserviceimpl-expected.xml", "studyserviceimpl-setup.xml"
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class StudyServiceImplTest {

	@Autowired
	private StudyService studyService;

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
	// TODO:- Rename the "studyserviceimpl-testsavestudy-expected.xml" to be
	// "studyserviceimpl-addStudy-expected.xml"
	@Test
	@DatabaseSetup("service-empty.xml")
	@ExpectedDatabase(value = "studyserviceimpl-testsavestudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addStudy() throws Exception {
		studyService.addOrUpdateStudy(ModelObjectCreationHelper.createTestStudy());
	}

	// TODO:- Rename the "studyserviceimpl-testupdatestudy-expected.xml" to be
	// "studyserviceimpl-updateStudy-expected.xml"
	// and "studyserviceimpl-testupdatestudy-setup.xml" to be
	// "studyserviceimpl-updateStudy-setup.xml"
	@Test
	@DatabaseSetup("studyserviceimpl-testupdatestudy-setup.xml")
	@ExpectedDatabase(value = "studyserviceimpl-testupdatestudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void updateStudy() throws Exception {
		Study study = studyService.getStudy(1);
		study.setDescription("Updated Study Description");
		study.setName("Updated Study Name");

		studyService.addOrUpdateStudy(study);
	}

	@Test
	@DatabaseSetup("studyserviceimpl-addUserToStudy-setup.xml")
	@ExpectedDatabase(value = "studyserviceimpl-addUserToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addUserToStudy() throws Exception {
		Study study = studyService.addUserToStudy(1010l, 1000l);
		assertNotNull(study);
		Set<User> users = study.getUsers();
		assertTrue(users.size() == 1);
		for (User user : users) {
			assertTrue(user.getId() == 1010l);
			assertEquals(user.getUserName(), "admin100");
			assertEquals(user.getPasswordHash(), "adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524");
			assertEquals(user.getEmail(), "first.last@manchester.ac.uk");
			assertEquals(user.getTitle(), "Mr");
			assertEquals(user.getFirstName(), "First");
			assertEquals(user.getLastName(), "Last");
			assertEquals(user.getRole(), User.Role.ROLE_ADMIN);
			assertNull(user.getPasswordResetDate());
			assertNull(user.getPasswordResetGUID());
		}
		assertTrue(study.getId() == 1000l);
		assertEquals(study.getName(), "Study Name 1");
		assertEquals(study.getDescription(), "Study Description");
	}

	@Test
	@DatabaseSetup("studyserviceimpl-addParticipantToStudy-setup.xml")
	@ExpectedDatabase(value = "studyserviceimpl-addParticipantToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addParticipantToStudy() throws Exception {
		Study study = studyService.addParticipantToStudy(1050l, 1000l);
		assertNotNull(study);
		Set<Participant> participants = study.getParticipants();
		assertTrue(participants.size() == 1);
		for (Participant participant : participants) {
			assertTrue(participant.getId() == 1050l);
			assertEquals(participant.getName(), "one name");
			assertEquals(participant.getAddress(), "one address");
			assertEquals(participant.getContactNumber(), "01132456789");
			assertTrue(participant.getStudy().getId() == 1000l);
		}
		assertTrue(study.getId() == 1000l);
		assertEquals(study.getName(), "Study Name 1");
		assertEquals(study.getDescription(), "Study Description");
	}

}