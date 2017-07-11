package com.medplus.journals.service;

import com.medplus.journals.Application;
import com.medplus.journals.model.Role;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.repository.CategoryRepository;

import com.medplus.journals.repository.SubscriptionRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test-application.properties")
@Transactional
public class UserServiceImplIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  public void findUserById_givenUserId_thenReturnsUser() {
    UserEntity user = userService.findById(1l);

    assertThat(user, is(notNullValue()));
    assertThat(user.getEmail(), is("publisher1@gmail.com"));
    assertThat(user.getRole(), is(Role.PUBLISHER));
    assertThat(user.getEnabled(), is(true));
    assertThat(user.getLoginName(), is("publisher1"));
  }

  @Test
  public void getUserByLoginName_givenLoginName_thenReturnsUser() {
    Optional<UserEntity> user = userService.getUserByLoginName("publisher1");

    assertThat(user.get(), is(notNullValue()));
    assertThat(user.get().getEmail(), is("publisher1@gmail.com"));
    assertThat(user.get().getRole(), is(Role.PUBLISHER));
    assertThat(user.get().getEnabled(), is(true));
    assertThat(user.get().getLoginName(), is("publisher1"));
  }


  @Test
  public void getAllUsers_returnsAllUsers() {
    List<UserEntity> users = userService.getAllUsers();

    assertThat(users.size(), is(4));
  }

  @Test
  public void subscribe_givenUserAndCategory_whenSuccessful_subscriptionIsAdded() {
    Optional<UserEntity> user = userService.getUserByLoginName("user1");

    userService.subscribe(user.get(), 1L);

    List<SubscriptionEntity> subscriptions = subscriptionRepository.findByCategory(categoryRepository.findOne(1L));
    assertThat(subscriptions, is(Matchers.notNullValue()));
    assertThat(subscriptions.size(), is(1));
  }

  @Test
  public void subscribe_givenCategoryDoesNotExist_thenExceptionIsThrown() {
    Optional<UserEntity> user = userService.getUserByLoginName("user1");

    userService.subscribe(user.get(), 5L);
  }
}
