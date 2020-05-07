package org.chi.dpuk.sensing.platform.dataSet;

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
//import java.util.Date;

/*
 * As we are using Spring, we use the Spring unit test runner
 * To get spring to do the autowiring for us, we pass it the necessary xml files in the ContextConfiguration annotation
 * The TestExecutionListeners specified here are necessary for the DBUnit / Spring integration to work correctly
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class DataSetServiceImplTest {

	@Autowired
	private DataSetService dataSetService;

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
	@ExpectedDatabase(value = "dataSetServiceImpl-addDataSet-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("service-empty.xml")
	public void addDataSet() throws Exception {
		dataSetService.addDataSet(ModelObjectCreationHelper.createTestDataSet());
	}

}