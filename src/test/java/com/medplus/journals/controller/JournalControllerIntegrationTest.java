package com.medplus.journals.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.medplus.journals.Application;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
public class JournalControllerIntegrationTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    System.setProperty("upload-dir", "/journals");
  }

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  @Test
  @WithUserDetails(value = "user1")
  public void browse_whenSuccessful_thenReturnsJournalsSubscribedByUser() throws Exception {
    mockMvc.perform(get("/rest/journals"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$[0].name", Matchers.is("Medicine")))
        .andExpect(jsonPath("$[0].notified", Matchers.is(false)))
        .andExpect(jsonPath("$[0].publisher", Matchers.is("Test Publisher1")))
        .andExpect(jsonPath("$[0].category", Matchers.is("therapy")))
        .andExpect(jsonPath("$[0].id", Matchers.is(1)));
  }

  @Test
  @WithUserDetails(value = "user1")
  public void publishedList_whenUserIsNotAPublisher_thenReturns403Forbidden() throws Exception {
    mockMvc.perform(get("/rest/journals/published"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(value = "publisher1")
  public void publishedList_whenUserIsAPublisherAndHasJournals_thenReturnsJournalList() throws Exception {
    mockMvc.perform(get("/rest/journals/published"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", Matchers.is("Medicine")))
        .andExpect(jsonPath("$[0].notified", Matchers.is(false)))
        .andExpect(jsonPath("$[0].publisher", Matchers.is("Test Publisher1")))
        .andExpect(jsonPath("$[0].category", Matchers.is("therapy")))
        .andExpect(jsonPath("$[0].id", Matchers.is(1)))
        .andExpect(jsonPath("$[1].name", Matchers.is("Test Journal")))
        .andExpect(jsonPath("$[1].notified", Matchers.is(false)))
        .andExpect(jsonPath("$[1].publisher", Matchers.is("Test Publisher1")))
        .andExpect(jsonPath("$[1].category", Matchers.is("stomatology")))
        .andExpect(jsonPath("$[1].id", Matchers.is(2)));
  }

  @Test
  @WithUserDetails(value = "user1")
  public void unPublish_whenUserIsNotAPublisher_thenThrows403Forbidden() throws Exception {
    mockMvc.perform(delete("/rest/journals/1/unPublish"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(value = "publisher2")
  public void unPublish_whenPublisherUnPublishesAnotherPublishersJournal_thenThrowsException() throws Exception {
    mockMvc.perform(delete("/rest/journals/1/unPublish"))
        .andExpect(status().is5xxServerError());
  }

  @Test
  @WithUserDetails(value = "publisher1")
  public void unPublish_whenPublisherUnPublishesJournal_thenJournalIsUnpublished() throws Exception {
    mockMvc.perform(delete("/rest/journals/1/unPublish"))
        .andExpect(status().isOk());
  }
}
