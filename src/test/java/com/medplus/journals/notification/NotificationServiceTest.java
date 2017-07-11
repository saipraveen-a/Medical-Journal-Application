package com.medplus.journals.notification;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.mail.Mail;
import com.medplus.journals.mail.service.MailService;
import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.Role;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.service.JournalService;
import com.medplus.journals.service.SubscriptionService;
import com.medplus.journals.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations="classpath:test-application.properties")
public class NotificationServiceTest {
  private static final Date PUBLISH_DATE = new Date();
  private static final String PUBLISHER = "publisher";
  private static final String TAB = "\t";
  private static final String NEWLINE = "\n";

  @Mock
  private JournalService journalService;
  @Mock
  private UserService userService;
  @Mock
  private MailService mailService;
  @Mock
  private SubscriptionService subscriptionService;

  private NotificationService notificationService;

  @Before
  public void setUp() {
    notificationService = new NotificationService(journalService, userService, mailService, subscriptionService);

    when(journalService.findUnNotifiedJournals()).thenReturn(unNotifiedJournals());
    when(userService.getAllUsers()).thenReturn(allUsers());
    when(subscriptionService.findSubscriptionsByCategory(1L)).thenReturn(subscriptionsForCategory(1L));
  }

  private List<JournalDTO> unNotifiedJournals() {
    List<JournalDTO> unNotifiedJournals = new ArrayList<>();

    JournalDTO journal1 = buildJournal(1);
    JournalDTO journal2 = buildJournal(2);
    JournalDTO journal3 = buildJournal(3);

    unNotifiedJournals.add(journal1);
    unNotifiedJournals.add(journal2);
    unNotifiedJournals.add(journal3);

    return unNotifiedJournals;
  }

  private JournalDTO buildJournal(long id) {
    return new JournalDTO.Builder()
        .withId(id)
        .withName("name-" + id)
        .withPublisher(PUBLISHER)
        .withCategory("category-" + id)
        .withPublishDate(PUBLISH_DATE)
        .withNotified(false)
        .build();
  }

  private List<UserEntity> allUsers() {
    List<UserEntity> users = new ArrayList<>();

    UserEntity user1 = buildUser(1, Role.USER);
    UserEntity user2 = buildUser(2, Role.USER);
    UserEntity publisher1 = buildUser(3, Role.PUBLISHER);

    users.add(user1);
    users.add(user2);
    users.add(publisher1);

    return users;
  }

  private UserEntity buildUser(long id, Role role) {
    UserEntity user = new UserEntity();
    user.setId(id);
    user.setLoginName("loginName-" + id);
    user.setEmail("email-" + id + "@email.com");
    user.setPwd("password-" + id);
    user.setRole(role);
    user.setEnabled(true);
    return user;
  }

  private List<SubscriptionEntity> subscriptionsForCategory(long id) {
    List<SubscriptionEntity> subscriptions = new ArrayList<>();

    subscriptions.add(buildSubscription(1L));
    subscriptions.add(buildSubscription(2L));

    return subscriptions;
  }

  private SubscriptionEntity buildSubscription(long id) {
    SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
    subscriptionEntity.setId(id);
    subscriptionEntity.setUser(buildUser(1L, Role.USER));
    subscriptionEntity.setCategory(buildCategory(1L, "category1"));

    return subscriptionEntity;
  }

  private CategoryEntity buildCategory(long id, String name) {
    CategoryEntity category = new CategoryEntity();
    category.setId(id);
    category.setName(name);
    return category;
  }

  @Test
  public void sendEmailNotifications_whenInvoked_thenSendsNotificationsToAllUsers() {
    notificationService.sendEmailNotifications();

    verify(mailService, times(1)).sendEmail(refEq(expectedMailContent(allUsers(), unNotifiedJournals())));
    verify(journalService, times(3)).updateJournalNotified(any(JournalDTO.class));
  }

  private Mail expectedMailContent(List<UserEntity> users, List<JournalDTO> journals) {
    Mail mail = new Mail();

    mail.setMailFrom("journaltestcrossover@gmail.com");
    mail.setMailSubject("New Journals Published");
    mail.setMailTo(extractEmailIds(users));
    mail.setMailContent(extractMailContent(journals));

    return mail;
  }

  private List<String> extractEmailIds(List<UserEntity> users) {
    List<String> emails = new ArrayList<>();

    for (UserEntity user : users) {
      emails.add(user.getEmail());
    }
    return emails;
  }

  private String extractMailContent(List<JournalDTO> journals) {
    StringBuilder mailContentBuilder = new StringBuilder();
    for (JournalDTO journal : journals) {
      mailContentBuilder.append("Journal Name: " + journal.getName()).append(TAB);
      mailContentBuilder.append("Journal Category:" + journal.getCategory()).append(TAB);
      mailContentBuilder.append("Published By: " + journal.getPublisher()).append(NEWLINE).append(NEWLINE);
    }
    return mailContentBuilder.toString();
  }

  @Test
  public void sendNotificationsToUsersSubscribedForCategory_givenCategory_thenSendsNotificationsToSubscribedUsers() {
    notificationService.sendNotificationsToUsersSubscribedForCategory(1L, "some mail content");

    verify(mailService, times(1)).sendEmail(refEq(expectedMailContent(Arrays.asList("email-1@email.com"),
        "some mail content")));
  }

  private Mail expectedMailContent(List<String> emails, String mailContent) {
    Mail mail = new Mail();

    mail.setMailFrom("journaltestcrossover@gmail.com");
    mail.setMailSubject("New Journals Published");
    mail.setMailTo(emails);
    mail.setMailContent(mailContent);

    return mail;
  }
}
