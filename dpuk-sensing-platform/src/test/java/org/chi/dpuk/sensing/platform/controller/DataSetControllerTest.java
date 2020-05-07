package org.chi.dpuk.sensing.platform.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * This class tests the dataSetController methods for handling datasets. This
 * incorporates testing of the REST interfaces.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml", "classpath:testSecurity.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class DataSetControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

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
	@DatabaseSetup("dataSetController-getDataSet-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getDataSet() throws Exception {
		mockMvc.perform(get("/api/dataSet/200").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.id", is(200)))
				.andExpect(jsonPath("$.payload", notNullValue()));
	}

	@Test
	@DatabaseSetup("dataSetController-getDataSourceForDataSet-setup.xml")
	@DatabaseTearDown("controller-empty.xml")
	public void getDataSourceForDataSet() throws Exception {
		mockMvc.perform(get("/api/dataSet/dataSource/1100").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1000))).andExpect(jsonPath("$.name", is("DataSource Name 1")))
				.andExpect(jsonPath("$.description", is("DataSource Description 1")))
				.andExpect(jsonPath("$.type", is("DataSource Type 1")))
				.andExpect(jsonPath("$.userName", is("DataSourceUserName1")));
	}

}