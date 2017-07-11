package com.medplus.journals.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.medplus.journals.Application;
import com.medplus.journals.model.SubscriptionEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test-application.properties")
public class SubscriptionServiceImplIntegrationTest {
  @Autowired
  private SubscriptionService subscriptionService;

  @Test
  public void getSubscriptionsByCategory_givenCategory_thenReturnsSubscriptionsForCategory() {
    List<SubscriptionEntity> subscriptions = subscriptionService.findSubscriptionsByCategory(3L);

    assertThat(subscriptions, is(notNullValue()));
    assertThat(subscriptions.size(), is(1));
  }
}
