package org.chi.dpuk.sensing.platform.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.chi.dpuk.sensing.platform.dataSource.DataSourceService;
import org.chi.dpuk.sensing.platform.dto.PayloadDTO;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.participant.ParticipantService;
import org.chi.dpuk.sensing.platform.study.StudyService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * This class tests the dataSourceController methods for handling users. This
 * incorporates testing of the REST interfaces.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class DataSourceControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private StudyService studyService;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin",
				"adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524"));
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "dataSourceController-addDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSource() throws Exception {
		DataSource dataSource = new DataSource();
		dataSource.setName("DataSource Name");
		dataSource.setDescription("DataSource Description");
		dataSource.setType("DataSource Type");
		dataSource.setUserName("DataSourceUserName");
		dataSource.setPassword("DataSource Password");
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource))).andExpect(status().isOk()).andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourceWithNonAlphaNumericCharactersAsUserNameThrowsInvalidDataSourceUserNameException()
			throws Exception {
		DataSource dataSource = new DataSource();
		dataSource.setName("DataSource Name");
		dataSource.setDescription("DataSource Description");
		dataSource.setType("DataSource Type");
		dataSource.setUserName("DataSource UserName 123");
		dataSource.setPassword("DataSource Password");
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource))).andExpect(status().isBadRequest())
				.andExpect(status().reason("User name may only contain alphanumeric characters.")).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException-setup.xml")
	@ExpectedDatabase(value = "dataSourceController-addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException() throws Exception {
		// TODO: find a way of getting the datasource instance via a mockMvc get
		// request (rather than through the DataSourceService).
		DataSource persistedDataSource = dataSourceService.getDataSource(2);

		// TODO: find a way of getting the participant instance via a mockMvc
		// get
		// request (rather than through the ParticipantService).
		Participant participant = participantService.getParticipant(100);

		mockMvc.perform(post("/api/dataSource/addParticipantToDataSource/" + participant.getId().toString() + "/"
				+ persistedDataSource.getId().toString()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isForbidden())
				.andExpect(status()
						.reason("Data source with the same name cannot be added or updated to the same participant."))
				.andDo(print()).andReturn();
	}

	@Test
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourceWithInvalidName() throws Exception {
		exception.expect(NestedServletException.class);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8).content(
				"{\"name\": \"xx\",\"description\": \"DataSource Description\",\"type\": \"DataSource Type\",\"userName\": \"DataSourceUserName\",\"password\": \"DataSource Password\"}"))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-updateDataSource-setup.xml")
	@ExpectedDatabase(value = "dataSourceController-updateDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateDataSource() throws Exception {
		// TODO: find a way of getting the datasource instance via a mockMvc get
		// request (rather than through the DataSourceService).
		DataSource dataSource = dataSourceService.getDataSource(1);

		// TODO: find a way of getting the participant instance via a mockMvc
		// get
		// request (rather than through the ParticipantService).
		Participant participant = participantService.getParticipant(100);

		dataSource.setName("Updated DataSource Name");
		dataSource.setDescription("Updated DataSource Description");
		dataSource.setType("Updated DataSource Type");
		dataSource.setUserName("UpdatedDataSourceUserName");
		dataSource.setPassword("Updated DataSource Password");
		dataSource.setParticipant(participant);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource))).andExpect(status().isOk()).andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-getParticipantForDataSource-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getParticipantForDataSource() throws Exception {
		mockMvc.perform(get("/api/dataSource/participant/1100").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1050))).andExpect(jsonPath("$.name", is("one name")))
				.andExpect(jsonPath("$.address", is("one address")))
				.andExpect(jsonPath("$.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.nhsNumber", is("9999999999")));
	}

	@Test
	@DatabaseSetup("dataSourceController-addParticipantToDataSource-setup.xml")
	@ExpectedDatabase(value = "dataSourceController-addParticipantToDataSource-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantToDataSource() throws Exception {
		mockMvc.perform(post("/api/dataSource/addParticipantToDataSource/1050/1100").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1100))).andExpect(jsonPath("$.name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.userName", is("DataSourceUserName1")))
				.andExpect(jsonPath("$.participant.id", is(1050)))
				.andExpect(jsonPath("$.participant.name", is("one name")))
				.andExpect(jsonPath("$.participant.address", is("one address")))
				.andExpect(jsonPath("$.participant.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.participant.nhsNumber", is("9999999999")));
	}

	@Test
	@DatabaseSetup("dataSourceController-getAllDataSources-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getAllDataSources() throws Exception {
		mockMvc.perform(get("/api/dataSource").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(100))).andExpect(jsonPath("$[0].name", is("DataSource Name 1")))
				.andExpect(jsonPath("$[0].description", is("DataSource Description 1")))
				.andExpect(jsonPath("$[0].type", is("DataSource Type 1")))
				.andExpect(jsonPath("$[0].userName", is("DataSourceUserName1"))).andExpect(jsonPath("$[1].id", is(200)))
				.andExpect(jsonPath("$[1].name", is("DataSource Name 2")))
				.andExpect(jsonPath("$[1].description", is("DataSource Description 2")))
				.andExpect(jsonPath("$[1].type", is("DataSource Type 2")))
				.andExpect(jsonPath("$[1].userName", is("DataSourceUserName2"))).andExpect(jsonPath("$[2].id", is(300)))
				.andExpect(jsonPath("$[2].name", is("DataSource Name 3")))
				.andExpect(jsonPath("$[2].description", is("DataSource Description 3")))
				.andExpect(jsonPath("$[2].type", is("DataSource Type 3")))
				.andExpect(jsonPath("$[2].userName", is("DataSourceUserName3")));
	}

	@Test
	@DatabaseSetup("dataSourceController-getDataSource-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getDataSource() throws Exception {
		mockMvc.perform(get("/api/dataSource/200").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.id", is(200)))
				.andExpect(jsonPath("$.name", is("DataSource Name 2")))
				.andExpect(jsonPath("$.description", is("DataSource Description 2")))
				.andExpect(jsonPath("$.type", is("DataSource Type 2")))
				.andExpect(jsonPath("$.userName", is("DataSourceUserName2")));
	}

	@Test
	@DatabaseSetup("dataSourceController-getDataSetsForDataSource-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getDataSetsForDataSource() throws Exception {
		mockMvc.perform(get("/api/dataSource/dataSet/100").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1100)))
				.andExpect(jsonPath("$[1].id", is(1200)));
	}

	@Test
	@DatabaseSetup("dataSourceController-postDataSet-setup.xml")
	@ExpectedDatabase(value = "dataSourceController-postDataSet-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void postDataSet() throws Exception {
		// Saving via an API call to hash password for DataSource
		Study study = studyService.getStudy(1200);
		Participant participant = participantService.getParticipant(1250);
		participant.setStudy(study);
		participantService.addOrUpdateParticipant(participant);

		DataSource dataSource1 = new DataSource();
		dataSource1.setName("DataSource Name 1");
		dataSource1.setDescription("DataSource Description 1");
		dataSource1.setType("DataSource Type 1");
		dataSource1.setUserName("DataSourceUserName1");
		dataSource1.setPassword("DataSource Password 1");
		dataSource1.setParticipant(participant);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource1))).andExpect(status().isOk()).andReturn();

		DataSource dataSource2 = new DataSource();
		dataSource2.setName("DataSource Name 2");
		dataSource2.setDescription("DataSource Description 2");
		dataSource2.setType("DataSource Type 2");
		dataSource2.setUserName("DataSourceUserName2");
		dataSource2.setPassword("DataSource Password 2");
		dataSource2.setParticipant(participant);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource2))).andExpect(status().isOk()).andReturn();

		Long dataSourceId = dataSourceService.getAllDataSources().get(0).getId();

		PayloadDTO payloadDTO = new PayloadDTO();
		payloadDTO.setDataSourcePassword("DataSource Password 1");
		payloadDTO.setDataSourceUserName("DataSourceUserName1");
		payloadDTO.setEndDate("2015-10-15 15:15:00.456");
		payloadDTO.setPayload("xmxmxmxmxmxmxmxmxmxmxmxmxmx");
		payloadDTO.setPayloadContentType("test");
		payloadDTO.setPayloadNameWithExtension("test.txt");
		payloadDTO.setStartDate("2015-10-15 14:15:00.123");

		mockMvc.perform(post("/api/dataSource/postDataSet").content(new ObjectMapper().writeValueAsString(payloadDTO))
				.contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(dataSourceId.intValue())))
				.andExpect(jsonPath("$.name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.userName", is("DataSourceUserName1"))).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-postDataSetThrowsDataSetNotUniqueWithinAStudyException-setup.xml")
	@ExpectedDatabase(value = "dataSourceController-postDataSetThrowsDataSetNotUniqueWithinAStudyException-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void postDataSetThrowsDataSetNotUniqueWithinAStudyException() throws Exception {
		// Saving via an API call to hash password for DataSource
		Study study = studyService.getStudy(1200);
		Participant participant = participantService.getParticipant(1250);
		participant.setStudy(study);
		participantService.addOrUpdateParticipant(participant);

		DataSource dataSource1 = new DataSource();
		dataSource1.setName("DataSource Name 1");
		dataSource1.setDescription("DataSource Description 1");
		dataSource1.setType("DataSource Type 1");
		dataSource1.setUserName("DataSourceUserName1");
		dataSource1.setPassword("DataSource Password 1");
		dataSource1.setParticipant(participant);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource1))).andExpect(status().isOk()).andReturn();

		DataSource dataSource2 = new DataSource();
		dataSource2.setName("DataSource Name 2");
		dataSource2.setDescription("DataSource Description 2");
		dataSource2.setType("DataSource Type 2");
		dataSource2.setUserName("DataSourceUserName2");
		dataSource2.setPassword("DataSource Password 2");
		dataSource2.setParticipant(participant);
		mockMvc.perform(post("/api/dataSource").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(dataSource2))).andExpect(status().isOk()).andReturn();

		Long dataSourceId = dataSourceService.getAllDataSources().get(0).getId();

		PayloadDTO payloadDTO = new PayloadDTO();
		payloadDTO.setDataSourcePassword("DataSource Password 1");
		payloadDTO.setDataSourceUserName("DataSourceUserName1");
		payloadDTO.setEndDate("2015-10-15 15:15:00.456");
		payloadDTO.setPayload("xmxmxmxmxmxmxmxmxmxmxmxmxmx");
		payloadDTO.setPayloadContentType("test");
		payloadDTO.setPayloadNameWithExtension("test.txt");
		payloadDTO.setStartDate("2015-10-15 14:15:00.123");

		mockMvc.perform(post("/api/dataSource/postDataSet").content(new ObjectMapper().writeValueAsString(payloadDTO))
				.contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(dataSourceId.intValue())))
				.andExpect(jsonPath("$.name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.userName", is("DataSourceUserName1"))).andDo(print()).andReturn();

		// Try to add the same DataSet again
		mockMvc.perform(post("/api/dataSource/postDataSet").content(new ObjectMapper().writeValueAsString(payloadDTO))
				.contentType(APPLICATION_JSON_UTF8)).andExpect(status().isForbidden())
				.andExpect(status().reason("Data set should be unique within a study.")).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-postDataSetThrowsDataSourceAuthenticationException-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void postDataSetThrowsDataSourceAuthenticationException() throws Exception {
		PayloadDTO payloadDTO = new PayloadDTO();
		payloadDTO.setDataSourcePassword("DataSource Password 1");
		payloadDTO.setDataSourceUserName("DataSource UserName 5");
		payloadDTO.setEndDate("2015-10-15 15:15:00.456");
		payloadDTO.setPayload("xmxmxmxmxmxmxmxmxmxmxmxmxmx");
		payloadDTO.setPayloadContentType("test");
		payloadDTO.setPayloadNameWithExtension("test.txt");
		payloadDTO.setStartDate("2015-10-15 14:15:00.123");

		mockMvc.perform(post("/api/dataSource/postDataSet").param("payload", "")
				.param("startDate", "2015-10-15 14:15:00.123").param("endDate", "2015-10-15 15:15:00.456")
				.param("dataSourceUserName", "DataSource UserName 5")
				.param("dataSourcePassword", "DataSource Password 1")
				.content(new ObjectMapper().writeValueAsString(payloadDTO)).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isUnauthorized()).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("dataSourceController-doesDataSourceExistWhenDataSourceExists-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void doesDataSourceExistWhenDataSourceExists() throws Exception {
		mockMvc.perform(get("/api/dataSource/doesDataSourceExist").contentType(APPLICATION_JSON_UTF8).param("userName",
				"DataSourceUserName")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", is(Boolean.FALSE)));
	}

	@Test
	@DatabaseSetup("dataSourceController-doesDataSourceExistWhenDataSourceDoesNotExist-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void doesDataSourceExistWhenDataSourceDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/dataSource/doesDataSourceExist").contentType(APPLICATION_JSON_UTF8).param("userName",
				"DataSourceUserName")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", is(Boolean.TRUE)));
	}
}