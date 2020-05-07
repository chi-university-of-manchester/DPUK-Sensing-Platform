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
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.participant.ParticipantService;
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
 * This class tests the ParticipantController methods for handling participants.
 * This incorporates testing of the REST interfaces.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class ParticipantControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private DataSourceService dataSourceService;

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
	@ExpectedDatabase(value = "participantcontroller-addParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipant() throws Exception {
		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content("{\"name\":\"some name 1\",\"address\":\"some address 1\""
						+ ",\"contactNumber\":\"011   32456 78 9\",\"nhsNumber\":\"9999999999\"}"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "participantcontroller-addParticipantWithNullName-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNullName() throws Exception {
		Participant participant = new Participant();
		participant.setAddress("some address 1");
		participant.setContactNumber("01132456789");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "participantcontroller-addParticipantWithNullAddress-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNullAddress() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setContactNumber("01132456789");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "participantcontroller-addParticipantWithNullContactNumber-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNullContactNumber() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "participantcontroller-addParticipantWithNullNhsNumber-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNullNhsNumber() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setContactNumber("01132456789");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithContactNumberLessThan5() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setContactNumber("0113");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If a contact number is provided, it must be valid.")).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNhsNumberLessThan10() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setContactNumber("01134567890");
		participant.setNhsNumber("12345");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If an NHS number is provided, it must be a 10 digit number."))
				.andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantWithNhsNumberGreaterThan10() throws Exception {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setContactNumber("01134567890");
		participant.setNhsNumber("99999999991");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If an NHS number is provided, it must be a 10 digit number."))
				.andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-updateParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipant() throws Exception {
		// TODO:- Find a way of fetching the participant instance via a mockMvc
		// 'get' request (rather than through the ParticipantService).
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("(+0044)01132456789");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	// TODO:- Find a better way of testing null value in the expected xml as the
	// column is being skipped without being defined
	// Ideally it explicitly needs to be compared with null or checked to return
	// a null value
	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-updateParticipantWithNullName-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNullName() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName(null);
		participant.setAddress("updated some address 1");
		participant.setContactNumber("(+0044)01132456789");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-updateParticipantWithNullAddress-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNullAddress() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress(null);
		participant.setContactNumber("(+0044)01132456789");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-updateParticipantWithNullContactNumber-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNullContactNumber() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber(null);
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-updateParticipantWithNullNhsNumber-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNullNhsNumber() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("(+44)01132456789");
		participant.setNhsNumber(null);

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithContactNumberLessThan5() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("0113");
		participant.setNhsNumber("9999999999");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If a contact number is provided, it must be valid.")).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNhsNumberLessThan10() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("(+44)01132456789");
		participant.setNhsNumber("01234");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If an NHS number is provided, it must be a 10 digit number."))
				.andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-updateParticipant-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void updateParticipantWithNhsNumberGreaterThan10() throws Exception {
		Participant participant = participantService.getParticipant(100);
		participant.setName("updated some name 1");
		participant.setAddress("updated some address 1");
		participant.setContactNumber("(+44)01132456789");
		participant.setNhsNumber("12345678901");

		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(participant))).andExpect(status().isBadRequest())
				.andExpect(status().reason("If an NHS number is provided, it must be a 10 digit number."))
				.andDo(print()).andReturn();
	}

	// We need this test as a 3rd party can use this API and try to update an id
	// that does not exist
	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "controller-empty.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateNonExistingParticipant() throws Exception {
		exception.expect(NestedServletException.class);
		mockMvc.perform(post("/api/participant").contentType(APPLICATION_JSON_UTF8)
				.content("{\"id\":\"100\",\"name\":\"some name 1\",\"address\":\"some address 1\""
						+ ",\"contactNumber\":\"01132456789\",\"nhsNumber\":\"1234567890\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/api/participant/100").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getAllParticipantsWhenNoneExist() throws Exception {
		mockMvc.perform(get("/api/participant").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DatabaseSetup("participantcontroller-getAllParticipantsWhenManyExist-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getAllParticipantsWhenManyExist() throws Exception {
		mockMvc.perform(get("/api/participant").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(100))).andExpect(jsonPath("$[0].name", is("some name 1")))
				.andExpect(jsonPath("$[0].address", is("some address 1")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789")))
				.andExpect(jsonPath("$[0].nhsNumber", is("0123456789"))).andExpect(jsonPath("$[1].id", is(200)))
				.andExpect(jsonPath("$[1].name", is("some name 2")))
				.andExpect(jsonPath("$[1].address", is("some address 2")))
				.andExpect(jsonPath("$[1].contactNumber", is("01234567890")))
				.andExpect(jsonPath("$[1].nhsNumber", is("9999999999"))).andExpect(jsonPath("$[2].id", is(300)))
				.andExpect(jsonPath("$[2].name", is("some name 3")))
				.andExpect(jsonPath("$[2].address", is("some address 3")))
				.andExpect(jsonPath("$[2].contactNumber", is("0645464646")))
				.andExpect(jsonPath("$[2].nhsNumber", is("5555555555")));
	}

	@Test
	@DatabaseSetup("participantcontroller-getAllParticipantsWhenOnlyOneExists-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getAllParticipantsWhenOnlyOneExists() throws Exception {
		mockMvc.perform(get("/api/participant").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(1000))).andExpect(jsonPath("$[0].name", is("one name")))
				.andExpect(jsonPath("$[0].address", is("one address")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789")))
				.andExpect(jsonPath("$[0].nhsNumber", is("9999999999")));
	}

	@Test
	@DatabaseSetup("participantcontroller-getExistingParticipantById-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getExistingParticipantById() throws Exception {
		mockMvc.perform(get("/api/participant/100").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.id", is(100)))
				.andExpect(jsonPath("$.name", is("some name 1"))).andExpect(jsonPath("$.address", is("some address 1")))
				.andExpect(jsonPath("$.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.nhsNumber", is("0123456789")));

	}

	// TODO:- Re-factor this to further test the empty content as a json output
	// TODO:- Re-visit this test and the pattern itself as we should not be
	// expecting
	// "java.lang.IllegalArgumentException: json can not be null or empty in
	// this case".
	// We should be expecting an empty json object back.
	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getNonExistingParticipantById() throws Exception {
		exception.expect(IllegalArgumentException.class);
		mockMvc.perform(get("/api/participant/200").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getExistingParticipantByNameWhenNoneExist() throws Exception {
		mockMvc.perform(
				get("/api/participant").contentType(APPLICATION_JSON_UTF8).param("participantName", "some name"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DatabaseSetup("participantcontroller-getExistingParticipantByNameWhenManyExist-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getExistingParticipantByNameWhenManyExist() throws Exception {
		mockMvc.perform(
				get("/api/participant").contentType(APPLICATION_JSON_UTF8).param("participantName", "some name 1"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(100)))
				.andExpect(jsonPath("$[0].name", is("some name 1")))
				.andExpect(jsonPath("$[0].address", is("some address 1")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789"))).andExpect(jsonPath("$[1].id", is(200)))
				.andExpect(jsonPath("$[1].name", is("some name 1")))
				.andExpect(jsonPath("$[1].address", is("some address 2")))
				.andExpect(jsonPath("$[1].contactNumber", is("01234567890")));
	}

	@Test
	@DatabaseSetup("participantcontroller-getExistingParticipantByNameWhenOnlyOneExists-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getExistingParticipantByNameWhenOnlyOneExists() throws Exception {
		mockMvc.perform(get("/api/participant").contentType(APPLICATION_JSON_UTF8).param("participantName", "one name"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1000)))
				.andExpect(jsonPath("$[0].name", is("one name"))).andExpect(jsonPath("$[0].address", is("one address")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789")));
	}

	@Test
	@DatabaseSetup("participantcontroller-getDataSourcesForParticipantOnParticipantId-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getDataSourcesForParticipantOnParticipantId() throws Exception {
		mockMvc.perform(get("/api/participant/dataSource/1050").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1100)))
				.andExpect(jsonPath("$[0].name", is("DataSource Name 1")))
				.andExpect(jsonPath("$[0].description", is("DataSource Description 1")))
				.andExpect(jsonPath("$[0].type", is("DataSource Type 1")))
				.andExpect(jsonPath("$[0].userName", is("DataSourceUserName1")))
				.andExpect(jsonPath("$[1].id", is(1101))).andExpect(jsonPath("$[1].name", is("DataSource Name 2")))
				.andExpect(jsonPath("$[1].description", is("DataSource Description 2")))
				.andExpect(jsonPath("$[1].type", is("DataSource Type 2")))
				.andExpect(jsonPath("$[1].userName", is("DataSourceUserName2")));
	}

	@Test
	@DatabaseSetup("participantcontroller-addDataSourceToParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-addDataSourceToParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourceToParticipant() throws Exception {
		mockMvc.perform(
				post("/api/participant/addDataSourceToParticipant/1100/1050").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1050))).andExpect(jsonPath("$.name", is("one name")))
				.andExpect(jsonPath("$.address", is("one address")))
				.andExpect(jsonPath("$.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.nhsNumber", is("0123456789")))
				.andExpect(jsonPath("$.dataSources[0].id", is(1100)))
				.andExpect(jsonPath("$.dataSources[0].name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.dataSources[0].description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.dataSources[0].type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.dataSources[0].userName", is("DataSourceUserName1")));
	}

	@Test
	@DatabaseSetup("participantcontroller-addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourceWithTheSameNameToTheSameParticipantThrowsDataSourceSameNameException() throws Exception {
		// TODO: find a way of getting the datasource instance via a mockMvc get
		// request (rather than through the DataSourceService).
		DataSource persistedDataSource = dataSourceService.getDataSource(2);

		// TODO: find a way of getting the participant instance via a mockMvc
		// get
		// request (rather than through the ParticipantService).
		Participant participant = participantService.getParticipant(100);

		mockMvc.perform(post("/api/participant/addDataSourceToParticipant/" + persistedDataSource.getId().toString()
				+ "/" + participant.getId().toString()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isForbidden())
				.andExpect(status()
						.reason("Data source with the same name cannot be added or updated to the same participant."))
				.andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("participantcontroller-addDataSourcesToParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-addDataSourcesToParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addDataSourcesToParticipant() throws Exception {
		mockMvc.perform(
				post("/api/participant/addDataSourcesToParticipant/1100,1200/1500").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1500))).andExpect(jsonPath("$.name", is("one name")))
				.andExpect(jsonPath("$.address", is("one address")))
				.andExpect(jsonPath("$.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.nhsNumber", is("0123456789")))
				.andExpect(jsonPath("$.dataSources[0].id", is(1100)))
				.andExpect(jsonPath("$.dataSources[0].name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.dataSources[0].description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.dataSources[0].type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.dataSources[0].userName", is("DataSourceUserName1")))
				.andExpect(jsonPath("$.dataSources[1].id", is(1200)))
				.andExpect(jsonPath("$.dataSources[1].name", is("DataSource Name 2")))
				.andExpect(jsonPath("$.dataSources[1].description", is("DataSource Description 2")))
				.andExpect(jsonPath("$.dataSources[1].type", is("DataSource Type 2")))
				.andExpect(jsonPath("$.dataSources[1].userName", is("DataSourceUserName2")));
	}

	@Test
	@DatabaseSetup("participantcontroller-getStudyForParticipantOnParticipantId-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getStudyForParticipantOnParticipantId() throws Exception {
		mockMvc.perform(get("/api/participant/study/1050").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1000))).andExpect(jsonPath("$.name", is("Study Name")))
				.andExpect(jsonPath("$.description", is("Study Description")));
	}

	@Test
	@DatabaseSetup("participantcontroller-addStudyToParticipant-setup.xml")
	@ExpectedDatabase(value = "participantcontroller-addStudyToParticipant-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addStudyToParticipant() throws Exception {
		mockMvc.perform(post("/api/participant/addStudyToParticipant/1000/1050").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1050))).andExpect(jsonPath("$.name", is("one name")))
				.andExpect(jsonPath("$.address", is("one address")))
				.andExpect(jsonPath("$.contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.nhsNumber", is("0123456789"))).andExpect(jsonPath("$.study.id", is(1000)))
				.andExpect(jsonPath("$.study.name", is("Study Name")))
				.andExpect(jsonPath("$.study.description", is("Study Description")));
	}

}