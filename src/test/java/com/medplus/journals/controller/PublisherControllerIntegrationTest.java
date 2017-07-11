package com.medplus.journals.controller;

import com.medplus.journals.Application;

import org.apache.commons.io.IOUtils;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
public class PublisherControllerIntegrationTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @WithUserDetails("publisher1")
  public void handleFileUpload_whenUserUploadsFile_thenJournalIsPublished() throws Exception {
    InputStream is = PublisherControllerIntegrationTest.class.getClassLoader().getResourceAsStream(
        "journals/09628d25-ea42-490e-965d-cd4ffb6d4e9d.pdf");
    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/publisher/publish")
        .file("file", IOUtils.toByteArray(is))
        .param("name", "Journal For Osteoporosis")
        .param("category", "1"))
        .andExpect(redirectedUrl("/publisher/browse"));

    mockMvc.perform(get("/rest/journals/published"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(3)));
  }

  private static ResultMatcher redirectedUrl(final String expectedUrl) {
    return new ResultMatcher() {
      public void match(MvcResult result) {
        assertEquals(result.getResponse().getRedirectedUrl(), expectedUrl);
      }
    };
  }
}
