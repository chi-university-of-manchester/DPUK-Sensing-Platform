package org.chi.dpuk.sensing.platform.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class UserServiceImplTest {

	@Autowired
	private UserService userService;

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
	@DatabaseSetup("userserviceimpl-setup.xml")
	@ExpectedDatabase(value = "userserviceimpl-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addUser() throws Exception {
		userService.saveUser(ModelObjectCreationHelper.createTestResearcher());
	}

	@Test
	@DatabaseSetup("userserviceimpl-addStudyToUser-setup.xml")
	@ExpectedDatabase(value = "userserviceimpl-addStudyToUser-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addStudyToUser() throws Exception {
		User user = userService.addStudyToUser(1000l, 1010l);
		assertNotNull(user);
		Set<Study> studies = user.getStudies();
		assertTrue(studies.size() == 1);
		for (Study study : studies) {
			assertTrue(study.getId() == 1000l);
			assertEquals(study.getName(), "Study Name 1");
			assertEquals(study.getDescription(), "Study Description");
		}
		assertTrue(user.getId() == 1010l);
		assertEquals(user.getUserName(), "admin100");
		assertEquals(user.getEmail(), "first.last@manchester.ac.uk");
		assertEquals(user.getTitle(), "Mr");
		assertEquals(user.getFirstName(), "First");
		assertEquals(user.getLastName(), "Last");
		assertEquals(user.getRole(), User.Role.ROLE_ADMIN);
		assertNull(user.getPasswordResetDate());
		assertNull(user.getPasswordResetGUID());
	}

}