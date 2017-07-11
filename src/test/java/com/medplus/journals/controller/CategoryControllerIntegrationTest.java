package com.medplus.journals.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.medplus.journals.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test-application.properties")
@Transactional
public class CategoryControllerIntegrationTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void getCategories() throws Exception {
		mockMvc.perform(get("/categories")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("surgery")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("ophthalmology")))
				.andExpect(jsonPath("$[2].id", is(3)))
				.andExpect(jsonPath("$[2].name", is("therapy")))
				.andExpect(jsonPath("$[3].id", is(4)))
				.andExpect(jsonPath("$[3].name", is("stomatology")))
				.andExpect(jsonPath("$[4].id", is(5)))
				.andExpect(jsonPath("$[4].name", is("cardiology")));
	}

	@Test
	@WithUserDetails("user1")
	public void getUserSubscriptions_whenSuccessful_thenReturnsCategoriesSubscribedByUser() throws Exception {
		mockMvc.perform(get("/categories/subscriptions")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("surgery")))
				.andExpect(jsonPath("$[0].active", is(false)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("ophthalmology")))
				.andExpect(jsonPath("$[1].active", is(false)))
				.andExpect(jsonPath("$[2].id", is(3)))
				.andExpect(jsonPath("$[2].name", is("therapy")))
				.andExpect(jsonPath("$[2].active", is(true)))
				.andExpect(jsonPath("$[3].id", is(4)))
				.andExpect(jsonPath("$[3].name", is("stomatology")))
				.andExpect(jsonPath("$[3].active", is(false)))
				.andExpect(jsonPath("$[4].id", is(5)))
				.andExpect(jsonPath("$[4].name", is("cardiology")))
				.andExpect(jsonPath("$[4].active", is(false)));
	}

	@Test
	@WithUserDetails("user1")
	public void subscribe_whenSuccessful_thenSubscribesUserForCategory() throws Exception {
		mockMvc.perform(post("/categories/1/subscribe")).andExpect(status().isOk());

		mockMvc.perform(get("/categories/subscriptions")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("surgery")))
				.andExpect(jsonPath("$[0].active", is(true)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("ophthalmology")))
				.andExpect(jsonPath("$[1].active", is(false)))
				.andExpect(jsonPath("$[2].id", is(3)))
				.andExpect(jsonPath("$[2].name", is("therapy")))
				.andExpect(jsonPath("$[2].active", is(true)))
				.andExpect(jsonPath("$[3].id", is(4)))
				.andExpect(jsonPath("$[3].name", is("stomatology")))
				.andExpect(jsonPath("$[3].active", is(false)))
				.andExpect(jsonPath("$[4].id", is(5)))
				.andExpect(jsonPath("$[4].name", is("cardiology")))
				.andExpect(jsonPath("$[4].active", is(false)));
	}
}
