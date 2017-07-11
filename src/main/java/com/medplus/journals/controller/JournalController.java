package com.medplus.journals.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.repository.JournalRepository;
import com.medplus.journals.repository.UserRepository;
import com.medplus.journals.service.JournalService;
import com.medplus.journals.service.ServiceException;

import com.medplus.journals.model.*;
import com.medplus.journals.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.medplus.journals.repository.PublisherRepository;
import com.medplus.journals.service.CurrentUser;

@RestController
@RequestMapping("/rest/journals")
public class JournalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(JournalController.class);

	@Autowired
	private JournalRepository journalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private JournalService journalService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<JournalDTO>> browse(@AuthenticationPrincipal CurrentUser activeUser) {
		return ResponseEntity.ok(journalService.findJournalsSubscribedByUser(activeUser.getUser()));
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity renderDocument(@AuthenticationPrincipal CurrentUser activeUser, @PathVariable("id") Long id)
			throws IOException {
		JournalEntity journal = journalRepository.findOne(id);
		CategoryEntity category = journal.getCategory();
		UserEntity user = userRepository.findOne(activeUser.getUser().getId());
		List<SubscriptionEntity> subscriptions = user.getSubscriptions();
		Optional<SubscriptionEntity> subscription = subscriptions.stream()
				.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();
		if (subscription.isPresent() || journal.getPublisher().getId().equals(user.getId())) {
			File file = new File(FileUtils.getFileName(journal.getPublisher().getId(), journal.getUuid()));
			InputStream in = new FileInputStream(file);
			return ResponseEntity.ok(IOUtils.toByteArray(in));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/published", method = RequestMethod.GET, produces = "application/json")
	public List<JournalDTO> publishedList(@AuthenticationPrincipal CurrentUser activeUser) {
		Optional<PublisherEntity> publisher = publisherRepository.findByUser(activeUser.getUser());
		return journalService.findJournalsByPublisher(publisher.get());
	}

	@RequestMapping(value = "/{id}/unPublish", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('PUBLISHER')")
	public void unPublish(@PathVariable("id") Long id, @AuthenticationPrincipal CurrentUser activeUser) {
		Optional<PublisherEntity> publisher = publisherRepository.findByUser(activeUser.getUser());
		journalService.unPublish(publisher.get(), id);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public void handleNoSuchElementException(NoSuchElementException ex) {
		LOGGER.error("Logged in user is not a publisher", ex);
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleServiceException(ServiceException ex) {
		LOGGER.error("Could not unpublish journal", ex);
	}
}
