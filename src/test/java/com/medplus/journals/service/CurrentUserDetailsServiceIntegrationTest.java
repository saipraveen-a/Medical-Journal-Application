package com.medplus.journals.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.medplus.journals.Application;
import com.medplus.journals.model.Role;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test-application.properties")
public class CurrentUserDetailsServiceIntegrationTest {
  @Autowired
  private CurrentUserDetailsService userDetailsService;

  @Test
  public void loadUserByUserName_givenUsername_returnsUser() {
    CurrentUser user = userDetailsService.loadUserByUsername("user1");

    assertThat(user, is(notNullValue()));
    assertThat(user.getRole(), is(Role.USER));
    assertThat(user.getId(), is(3L));
    assertThat(user.getUser().getEmail(), is("user1@gmail.com"));
    assertThat(user.getUser().getLoginName(), is("user1"));
  }

  @Test
  public void loadUserByUserName_givenUsernameDoesNotExist_thenThrowsException() {
    try {
      userDetailsService.loadUserByUsername("userx");
      fail("Expected UsernameNotFoundException");
    } catch (UsernameNotFoundException ex) {

    }
  }
}
