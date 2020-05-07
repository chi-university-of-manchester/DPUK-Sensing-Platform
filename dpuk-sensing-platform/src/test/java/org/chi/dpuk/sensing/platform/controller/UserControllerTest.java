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

import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.chi.dpuk.sensing.platform.security.HashingUtility;
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
 * This class tests the UserController methods for handling users. This
 * incorporates testing of the REST interfaces.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class UserControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	/*
	 * MockMvc is the class within the Spring test framework that provides
	 * support for Controller testing
	 */
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// set up the security context with TestProvider logged in
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("admin", HashingUtility.hashPassword("admin123")));
	}

	@Test
	@DatabaseSetup("controller-empty.xml")
	@ExpectedDatabase(value = "usercontroller-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addAdminUserSuccessfully() throws Exception {
		User admin = new User();
		admin.setUserName("admin1");
		String hashedPassword = HashingUtility.hashPassword("admin123");
		admin.setPasswordHash(hashedPassword);
		admin.setEmail("rajesh.bhadra@manchester.ac.uk");
		admin.setRole(Role.ROLE_ADMIN);
		admin.setTitle("Mr");
		admin.setFirstName("Rajesh");
		admin.setLastName("Bhadra");

		mockMvc.perform(post("/api/user").contentType(APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(admin))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	@Test
	@DatabaseSetup("usercontroller-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getAllUsersSuccessfully() throws Exception {
		mockMvc.perform(get("/api/user").contentType(APPLICATION_JSON_UTF8).param("userName", "admin1"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(10))).andExpect(jsonPath("$.userName", is("admin1")))
				.andExpect(jsonPath("$.email", is("rajesh.bhadra@manchester.ac.uk")))
				.andExpect(jsonPath("$.title", is("Mr"))).andExpect(jsonPath("$.firstName", is("Rajesh")))
				.andExpect(jsonPath("$.lastName", is("Bhadra")))
				.andExpect(jsonPath("$.role", is(Role.ROLE_ADMIN.name())));
	}

	@Test
	@DatabaseTearDown("controller-empty.xml")
	public void addAnyUserWithJunkRoleFails() throws Exception {
		mockMvc.perform(post("/api/user").contentType(APPLICATION_JSON_UTF8)
				.content("{\"userName\":\"admin2\",\"passwordHash\":\"" + HashingUtility.hashPassword("admin123") + "\""
						+ ",\"email\":\"rajesh2.bhadra2@manchester.ac.uk\",\"title\":\"Mr\",\"firstName\":\"Rajesh2\",\"lastName\":\"Bhadra2\""
						+ ",\"role\":\"ROLE_JUNK\"}"))
				.andExpect(status().isBadRequest()).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("usercontroller-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void addUserWithExistingUsernameFails() throws Exception {
		exception.expect(NestedServletException.class);
		mockMvc.perform(post("/api/user").contentType(APPLICATION_JSON_UTF8)
				.content("{\"userName\":\"admin1\",\"passwordHash\":\"" + HashingUtility.hashPassword("admin123") + "\""
						+ ",\"email\":\"rajesh2.bhadra2@manchester.ac.uk\",\"title\":\"Mr\",\"firstName\":\"Rajesh2\",\"lastName\":\"Bhadra2\""
						+ ",\"role\":\"ROLE_ADMIN\"}"))
				.andExpect(status().isInternalServerError()).andDo(print()).andReturn();
	}

	@Test
	@DatabaseSetup("usercontroller-getStudiesForUserOnUserId-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getStudiesForUserOnUserId() throws Exception {
		mockMvc.perform(get("/api/user/study/1001").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1000))).andExpect(jsonPath("$[0].name", is("Study Name1")))
				.andExpect(jsonPath("$[0].description", is("Study Description1")))
				.andExpect(jsonPath("$[1].id", is(1010))).andExpect(jsonPath("$[1].name", is("Study Name2")))
				.andExpect(jsonPath("$[1].description", is("Study Description2")));
	}

	@Test
	@DatabaseSetup("usercontroller-getStudiesForUserOnUserName-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getStudiesForUserOnUserName() throws Exception {
		mockMvc.perform(get("/api/user/study").contentType(APPLICATION_JSON_UTF8).param("userName", "admin100"))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1010)))
				.andExpect(jsonPath("$[0].name", is("Study Name 1")))
				.andExpect(jsonPath("$[0].description", is("Study Description 1")))
				.andExpect(jsonPath("$[1].id", is(1020))).andExpect(jsonPath("$[1].name", is("Study Name 2")))
				.andExpect(jsonPath("$[1].description", is("Study Description 2")));
	}

	@Test
	@DatabaseSetup("usercontroller-addStudyToUser-setup.xml")
	@ExpectedDatabase(value = "usercontroller-addStudyToUser-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addStudyToUser() throws Exception {
		mockMvc.perform(post("/api/user/addStudyToUser/1000/1010").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.studies[0].id", is(1000)))
				.andExpect(jsonPath("$.studies[0].name", is("Study Name 1")))
				.andExpect(jsonPath("$.studies[0].description", is("Study Description")))
				.andExpect(jsonPath("$.id", is(1010))).andExpect(jsonPath("$.userName", is("admin100")))
				.andExpect(jsonPath("$.email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$.title", is("Mr"))).andExpect(jsonPath("$.firstName", is("First")))
				.andExpect(jsonPath("$.lastName", is("Last"))).andExpect(jsonPath("$.role", is("ROLE_ADMIN")));
	}

	@Test
	@DatabaseSetup("usercontroller-addStudiesToUser-setup.xml")
	@ExpectedDatabase(value = "usercontroller-addStudiesToUser-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@DatabaseTearDown("controller-empty.xml")
	public void addStudiesToUser() throws Exception {
		mockMvc.perform(post("/api/user/addStudiesToUser/1000,1020/1010").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.studies[0].id", is(1000)))
				.andExpect(jsonPath("$.studies[0].name", is("Study Name 1")))
				.andExpect(jsonPath("$.studies[0].description", is("Study Description 1")))
				.andExpect(jsonPath("$.studies[1].id", is(1020)))
				.andExpect(jsonPath("$.studies[1].name", is("Study Name 2")))
				.andExpect(jsonPath("$.studies[1].description", is("Study Description 2")))
				.andExpect(jsonPath("$.id", is(1010))).andExpect(jsonPath("$.userName", is("admin100")))
				.andExpect(jsonPath("$.email", is("first.last@manchester.ac.uk")))
				.andExpect(jsonPath("$.title", is("Mr"))).andExpect(jsonPath("$.firstName", is("First")))
				.andExpect(jsonPath("$.lastName", is("Last"))).andExpect(jsonPath("$.role", is("ROLE_ADMIN")));
	}

}