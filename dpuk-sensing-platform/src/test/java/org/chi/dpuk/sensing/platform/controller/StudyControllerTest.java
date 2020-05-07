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

import org.chi.dpuk.sensing.platform.model.Study;
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
 * This class tests the StudyController methods for handling users. This
 * incorporates testing of the REST interfaces.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class StudyControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

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
	@ExpectedDatabase(value = "studycontroller-addStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addStudy() throws Exception {
		Study study = new Study();
		study.setName("Study Name 1");
		study.setDescription("Description of study 1.");

		mockMvc.perform(post("/api/study").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(study))).andExpect(status().isOk())
				// .andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseTearDown("controller-empty.xml")
	public void addStudyWithExistingStudyName() throws Exception {
		Study study = new Study();
		study.setName("Study Name 1");
		study.setDescription("Description of study 1.");

		mockMvc.perform(post("/api/study").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(study))).andExpect(status().isOk())
				// .andDo(print())
				.andReturn();
		exception.expect(NestedServletException.class);
		mockMvc.perform(post("/api/study").contentType(APPLICATION_JSON_UTF8)
				.content("{\"name\":\"Study Name 1\",\"description\":\"Description of study 1.\"}"))
				.andExpect(status().isInternalServerError()).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("studycontroller-updatestudy-setup.xml")
	@ExpectedDatabase(value = "studycontroller-updatestudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void updateStudy() throws Exception {
		// TODO: find a way of getting the study instance via a mockMvc 'get'
		// request (rather than through the DataSourceService).
		Study study = studyService.getStudy(1);

		study.setName("Updated Study Name");
		study.setDescription("Updated Study Description");

		mockMvc.perform(post("/api/study").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(study))).andExpect(status().isOk())
				// .andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("studycontroller-getUsersForStudyOnStudyId-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getUsersForStudyOnStudyId() throws Exception {
		mockMvc.perform(get("/api/study/user/1000").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1001))).andExpect(jsonPath("$[0].userName", is("admin100")))
				.andExpect(jsonPath("$[0].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$[0].email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$[0].title", is("Mr"))).andExpect(jsonPath("$[0].firstName", is("First")))
				.andExpect(jsonPath("$[0].lastName", is("Last"))).andExpect(jsonPath("$[0].role", is("ROLE_ADMIN")))
				.andExpect(jsonPath("$[1].id", is(1002))).andExpect(jsonPath("$[1].userName", is("admin101")))
				.andExpect(jsonPath("$[1].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$[1].email", is("first1.last1@manchester.ac.uk")))
				.andExpect(jsonPath("$[1].title", is("Mr"))).andExpect(jsonPath("$[1].firstName", is("First1")))
				.andExpect(jsonPath("$[1].lastName", is("Last1"))).andExpect(jsonPath("$[1].role", is("ROLE_ADMIN")));
	}

	@Test
	@DatabaseSetup("studycontroller-getUsersForStudyOnStudyName-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getUsersForStudyOnStudyName() throws Exception {
		mockMvc.perform(get("/api/study/user").contentType(APPLICATION_JSON_UTF8).param("studyName", "Study Name 1"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1010)))
				.andExpect(jsonPath("$[0].userName", is("admin100")))
				.andExpect(jsonPath("$[0].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$[0].email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$[0].title", is("Mr"))).andExpect(jsonPath("$[0].firstName", is("First")))
				.andExpect(jsonPath("$[0].lastName", is("Last"))).andExpect(jsonPath("$[0].role", is("ROLE_ADMIN")))
				.andExpect(jsonPath("$[1].id", is(1020))).andExpect(jsonPath("$[1].userName", is("admin101")))
				.andExpect(jsonPath("$[1].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$[1].email", is("first1.last1@manchester.ac.uk")))
				.andExpect(jsonPath("$[1].title", is("Mr"))).andExpect(jsonPath("$[1].firstName", is("First1")))
				.andExpect(jsonPath("$[1].lastName", is("Last1"))).andExpect(jsonPath("$[1].role", is("ROLE_ADMIN")));
	}

	@Test
	@DatabaseSetup("studycontroller-addUserToStudy-setup.xml")
	@ExpectedDatabase(value = "studycontroller-addUserToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addUserToStudy() throws Exception {
		mockMvc.perform(post("/api/study/addUserToStudy/1010/1000").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1000))).andExpect(jsonPath("$.name", is("Study Name 1")))
				.andExpect(jsonPath("$.description", is("Study Description")))
				.andExpect(jsonPath("$.users[0].id", is(1010)))
				.andExpect(jsonPath("$.users[0].userName", is("admin100")))
				.andExpect(jsonPath("$.users[0].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$.users[0].email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$.users[0].title", is("Mr")))
				.andExpect(jsonPath("$.users[0].firstName", is("First")))
				.andExpect(jsonPath("$.users[0].lastName", is("Last")))
				.andExpect(jsonPath("$.users[0].role", is("ROLE_ADMIN")));
	}

	@Test
	@DatabaseSetup("studycontroller-addUsersToStudy-setup.xml")
	@ExpectedDatabase(value = "studycontroller-addUsersToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addUsersToStudy() throws Exception {
		mockMvc.perform(post("/api/study/addUsersToStudy/1010,1020/1000").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1000))).andExpect(jsonPath("$.name", is("Study Name 1")))
				.andExpect(jsonPath("$.description", is("Study Description")))
				.andExpect(jsonPath("$.users[0].id", is(1010)))
				.andExpect(jsonPath("$.users[0].userName", is("admin100")))
				.andExpect(jsonPath("$.users[0].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$.users[0].email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$.users[0].title", is("Mr")))
				.andExpect(jsonPath("$.users[0].firstName", is("First")))
				.andExpect(jsonPath("$.users[0].lastName", is("Last")))
				.andExpect(jsonPath("$.users[0].role", is("ROLE_ADMIN"))).andExpect(jsonPath("$.users[1].id", is(1020)))
				.andExpect(jsonPath("$.users[1].userName", is("admin101")))
				.andExpect(jsonPath("$.users[1].passwordHash",
						is("adbcd080140eefb43aa31c75e7d3523b1c46a294bfe8847327190eed0a858524")))
				.andExpect(jsonPath("$.users[1].email", is("first1.last1@manchester.ac.uk")))
				.andExpect(jsonPath("$.users[1].title", is("Mr")))
				.andExpect(jsonPath("$.users[1].firstName", is("First1")))
				.andExpect(jsonPath("$.users[1].lastName", is("Last1")))
				.andExpect(jsonPath("$.users[1].role", is("ROLE_ADMIN")));
	}

	@Test
	@DatabaseSetup("studycontroller-getParticipantsForStudyOnStudyId-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getParticipantsForStudyOnStudyId() throws Exception {
		mockMvc.perform(get("/api/study/participant/1000").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1050)))
				.andExpect(jsonPath("$[0].name", is("one name"))).andExpect(jsonPath("$[0].address", is("one address")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789")))
				.andExpect(jsonPath("$[0].study.id", is(1000))).andExpect(jsonPath("$[1].id", is(1100)))
				.andExpect(jsonPath("$[1].name", is("two name"))).andExpect(jsonPath("$[1].address", is("two address")))
				.andExpect(jsonPath("$[1].contactNumber", is("01324252627")))
				.andExpect(jsonPath("$[1].study.id", is(1000)));
	}

	@Test
	@DatabaseSetup("studycontroller-getParticipantsForStudyOnStudyName-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getParticipantsForStudyOnStudyName() throws Exception {
		mockMvc.perform(
				get("/api/study/participant").contentType(APPLICATION_JSON_UTF8).param("studyName", "Study Name"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1250)))
				.andExpect(jsonPath("$[0].name", is("one name"))).andExpect(jsonPath("$[0].address", is("one address")))
				.andExpect(jsonPath("$[0].contactNumber", is("01132456789")))
				.andExpect(jsonPath("$[0].study.id", is(1200))).andExpect(jsonPath("$[1].id", is(1300)))
				.andExpect(jsonPath("$[1].name", is("two name"))).andExpect(jsonPath("$[1].address", is("two address")))
				.andExpect(jsonPath("$[1].contactNumber", is("01324252627")))
				.andExpect(jsonPath("$[1].study.id", is(1200)));
	}

	@Test
	@DatabaseSetup("studycontroller-addParticipantToStudy-setup.xml")
	@ExpectedDatabase(value = "studycontroller-addParticipantToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantToStudy() throws Exception {
		mockMvc.perform(post("/api/study/addParticipantToStudy/1250/1200").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1200))).andExpect(jsonPath("$.name", is("Study Name")))
				.andExpect(jsonPath("$.description", is("Study Description")))
				.andExpect(jsonPath("$.participants[0].id", is(1250)))
				.andExpect(jsonPath("$.participants[0].name", is("one name")))
				.andExpect(jsonPath("$.participants[0].address", is("one address")))
				.andExpect(jsonPath("$.participants[0].contactNumber", is("01132456789")));
	}

	@Test
	@DatabaseSetup("studycontroller-addParticipantsToStudy-setup.xml")
	@ExpectedDatabase(value = "studycontroller-addParticipantsToStudy-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addParticipantsToStudy() throws Exception {
		mockMvc.perform(post("/api/study/addParticipantsToStudy/1250,1300/1200").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1200))).andExpect(jsonPath("$.name", is("Study Name")))
				.andExpect(jsonPath("$.description", is("Study Description")))
				.andExpect(jsonPath("$.participants[0].id", is(1250)))
				.andExpect(jsonPath("$.participants[0].name", is("one name")))
				.andExpect(jsonPath("$.participants[0].address", is("one address")))
				.andExpect(jsonPath("$.participants[0].contactNumber", is("01132456789")))
				.andExpect(jsonPath("$.participants[1].id", is(1300)))
				.andExpect(jsonPath("$.participants[1].name", is("two name")))
				.andExpect(jsonPath("$.participants[1].address", is("two address")))
				.andExpect(jsonPath("$.participants[1].contactNumber", is("01324252728")));
	}

	@Test
	@DatabaseSetup("studycontroller-doesStudyExistWhenStudyExists-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void doesStudyExistWhenStudyExists() throws Exception {
		mockMvc.perform(get("/api/study/doesStudyExist").contentType(APPLICATION_JSON_UTF8).param("name", "Study Name"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", is(Boolean.FALSE)));
	}

	@Test
	@DatabaseSetup("studycontroller-doesStudyExistWhenStudyDoesNotExist-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void doesStudyExistWhenStudyDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/study/doesStudyExist").contentType(APPLICATION_JSON_UTF8).param("name", "Study Name"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", is(Boolean.TRUE)));
	}
}