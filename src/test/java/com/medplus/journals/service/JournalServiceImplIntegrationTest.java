package com.medplus.journals.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import com.medplus.journals.Application;
import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.model.JournalEntity;
import com.medplus.journals.model.PublisherEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.repository.JournalRepository;
import com.medplus.journals.repository.PublisherRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test-application.properties")
@Transactional
public class JournalServiceImplIntegrationTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  private JournalRepository journalRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PublisherRepository publisherRepository;

  @Autowired
  private JournalService service;

  @Test
  public void findJournalsSubscribedByUser_givenUser_thenReturnsJournalsSubscribedByUser() {
    UserEntity user = getUser("user1");

    List<JournalDTO> journals = service.findJournalsSubscribedByUser(user);
    assertThat(journals.size(), is(1));
    assertThat(journals.get(0).getCategory(), is("therapy"));
    assertThat(journals.get(0).isNotified(), is(false));
    assertThat(journals.get(0).getId(), is(1L));
    assertThat(journals.get(0).getName(), is("Medicine"));
    assertThat(journals.get(0).getPublisher(), is("Test Publisher1"));
  }

  @Test
  public void findJournalsByPublisher_givenPublisher_thenReturnsJournalsByPublisher() {
    UserEntity user = getUser("publisher1");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();

    List<JournalDTO> journals = service.findJournalsByPublisher(publisher);

    assertThat(journals.size(), is(2));

    assertThat(journals.get(0).getCategory(), is("therapy"));
    assertThat(journals.get(0).isNotified(), is(false));
    assertThat(journals.get(0).getId(), is(1L));
    assertThat(journals.get(0).getName(), is("Medicine"));
    assertThat(journals.get(0).getPublisher(), is("Test Publisher1"));

    assertThat(journals.get(1).getCategory(), is("stomatology"));
    assertThat(journals.get(1).isNotified(), is(false));
    assertThat(journals.get(1).getId(), is(2L));
    assertThat(journals.get(1).getName(), is("Test Journal"));
    assertThat(journals.get(1).getPublisher(), is("Test Publisher1"));
  }

  @Test
  public void publishJournal_givenPublisherPublishesJournal_thenJournalIsPublished() {
    UserEntity user = getUser("publisher1");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();
    JournalEntity journal = givenJournal();

    JournalDTO journaldto = service.publishJournal(publisher, journal, 2L);

    assertThat(journaldto, is(notNullValue()));
    assertThat(service.findJournalsByPublisher(publisher).size(), is(3));
  }

  @Test
  public void publishJournal_givenPublisherPublishesJournalForNonExistingCategory_thenExceptionIsThrown() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Category not found");

    UserEntity user = getUser("publisher1");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();
    JournalEntity journal = givenJournal();

    service.publishJournal(publisher, journal, 8L);
  }

  @Test
  public void unpublishJournal_givenPublisherUnPublishesJournal_thenJournalIsUnPublished() {
    UserEntity user = getUser("publisher1");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();

    service.unPublish(publisher, 2L);
  }

  @Test
  public void unpublishJournal_givenAnotherPublisherUnPublishesJournal_thenExceptionIsThrown() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Journal cannot be removed");

    UserEntity user = getUser("publisher2");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();

    service.unPublish(publisher, 2L);
  }

  @Test
  public void unpublishJournal_givenPublisherUnPublishesNotExistingJournal_thenExceptionIsThrown() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Journal doesn't exist");

    UserEntity user = getUser("publisher1");
    Optional<PublisherEntity> p = publisherRepository.findByUser(user);
    PublisherEntity publisher = p.get();

    service.unPublish(publisher, 4L);
  }

  private JournalEntity givenJournal() {
    JournalEntity journal = new JournalEntity();
    journal.setName("HeartStroke");
    journal.setUuid(UUID.randomUUID().toString());
    return journal;
  }

  protected UserEntity getUser(String name) {
    Optional<UserEntity> user = userService.getUserByLoginName(name);
    if (!user.isPresent()) {
      fail("User" + name + " doesn't exist");
    }
    return user.get();
  }

  @Test
  public void findUnNotifiedJournals_whenInvoked_thenReturnsUnNotifiedJournals() {
    List<JournalDTO> unNotifiedJournals = service.findUnNotifiedJournals();

    assertThat(unNotifiedJournals.size(), is(3));
  }

  @Test
  public void updateJournalNotified_whenInvoked_thenSetsNotifiedFlagToTrue() {
    UserEntity user = getUser("user1");
    List<JournalDTO> journals = service.findJournalsSubscribedByUser(user);

    JournalEntity journalEntity = journalRepository.findOne(journals.get(0).getId());
    assertThat(journalEntity.isNotified(), is(false));

    service.updateJournalNotified(journals.get(0));
    journalEntity = journalRepository.findOne(journals.get(0).getId());
    assertThat(journalEntity.isNotified(), is(true));
  }
}
