package com.medplus.journals.notification;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.mail.Mail;
import com.medplus.journals.mail.service.MailService;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.service.JournalService;
import com.medplus.journals.service.SubscriptionService;
import com.medplus.journals.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class NotificationService {
  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
  private static final String TAB = "\t";
  private static final String NEWLINE = "\n";

  private final JournalService journalService;
  private final UserService userService;
  private final MailService mailService;
  private final SubscriptionService subscriptionService;

  @Autowired
  public NotificationService(JournalService journalService,
                             UserService userService,
                             MailService mailService,
                             SubscriptionService subscriptionService) {
    this.journalService = journalService;
    this.userService = userService;
    this.mailService = mailService;
    this.subscriptionService = subscriptionService;
  }

  @Scheduled(cron = "0 0/2 * 1/1 * ?")
  // 0 0 12 * * ?
  public void sendEmailNotifications() {
    LOGGER.info("Sending email notifications for newly added journals");
    List<JournalDTO> journals = journalService.findUnNotifiedJournals();
    List<UserEntity> users = userService.getAllUsers();
    StringBuilder mailContentBuilder = new StringBuilder();
    for (JournalDTO journal : journals) {
      mailContentBuilder.append("Journal Name: " + journal.getName()).append(TAB);
      mailContentBuilder.append("Journal Category:" + journal.getCategory()).append(TAB);
      mailContentBuilder.append("Published By: " + journal.getPublisher()).append(NEWLINE).append(NEWLINE);
    }

    if (!journals.isEmpty()) {
      sendNotificationsToUsers(mailContentBuilder.toString(), users);
    }

    for (JournalDTO journal : journals) {
      journal.setNotified(true);
      journalService.updateJournalNotified(journal);
    }
  }

  @Async
  public void sendNotificationsToUsersSubscribedForCategory(Long categoryId, String mailContent) {
    LOGGER.info("Sending email notifications for new journal added for category {}", categoryId);
    List<SubscriptionEntity> subscriptions = subscriptionService.findSubscriptionsByCategory(categoryId);

    Set<UserEntity> users = new HashSet<>();
    for (SubscriptionEntity subscription : subscriptions) {
      users.add(subscription.getUser());
    }

    sendNotificationsToUsers(mailContent, new ArrayList<>(users));
  }

  private void sendNotificationsToUsers(String mailContent, List<UserEntity> users) {
    if (!users.isEmpty()) {
      List<String> toAddress = new ArrayList<>();

      for (UserEntity userEntity : users) {
        toAddress.add(userEntity.getEmail());
      }
      Mail mail = new Mail();
      mail.setMailFrom("journaltestcrossover@gmail.com");
      mail.setMailTo(toAddress);
      mail.setMailSubject("New Journals Published");
      mail.setMailContent(mailContent);
      mailService.sendEmail(mail);
    }
  }
}