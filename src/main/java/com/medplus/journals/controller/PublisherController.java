package com.medplus.journals.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;

import com.medplus.journals.notification.NotificationService;
import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.JournalEntity;
import com.medplus.journals.repository.CategoryRepository;
import com.medplus.journals.service.JournalService;
import com.medplus.journals.model.PublisherEntity;
import com.medplus.journals.repository.PublisherRepository;
import com.medplus.journals.service.CurrentUser;

import com.medplus.journals.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublisherController {

	private final static Logger log = Logger.getLogger(PublisherController.class);
	private static final String NEWLINE = "\n";

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private JournalService journalService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(method = RequestMethod.GET, value = "/publisher/publish")
	public String provideUploadInfo(Model model) {
		return "publisher/publish";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/publisher/publish")
	@PreAuthorize("hasRole('PUBLISHER')")
	public String handleFileUpload(@RequestParam("name") String name, @RequestParam("category")Long categoryId,
																 @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
																 @AuthenticationPrincipal CurrentUser activeUser) {

		Optional<PublisherEntity> publisher = publisherRepository.findByUser(activeUser.getUser());

		String uuid = UUID.randomUUID().toString();
		File dir = new File(FileUtils.getDirectory(publisher.get().getId()));
		FileUtils.createDirectoryIfNotExist(dir);

		File f = new File(FileUtils.getFileName(publisher.get().getId(), uuid));
		if (!file.isEmpty()) {
			try {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();
				JournalEntity journal = new JournalEntity();
				journal.setUuid(uuid);
				journal.setName(name);
				journalService.publishJournal(publisher.get(), journal, categoryId);

				sendNotificationsToSubscribedUsers(name, publisher.get(), categoryId);
				return "redirect:/publisher/browse";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message",
						"You failed to publish " + name + " => " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message",
					"You failed to upload " + name + " because the file was empty");
		}

		return "redirect:/publisher/publish";
	}

	private String generateMailContent(String name, String publisher, String category) {
		StringBuilder mailContentBuilder = new StringBuilder();
		mailContentBuilder.append("Journal Name: " + name).append(NEWLINE);
		mailContentBuilder.append("Journal Category:" + category).append(NEWLINE);
		mailContentBuilder.append("Published By: " + publisher);
		return mailContentBuilder.toString();
	}

	private void sendNotificationsToSubscribedUsers(String journalName, PublisherEntity publisher, Long categoryId) {
		CategoryEntity category = categoryRepository.findOne(categoryId);
		String mailContent = generateMailContent(journalName, publisher.getName(), category.getName());
		notificationService.sendNotificationsToUsersSubscribedForCategory(categoryId, mailContent);
	}
}