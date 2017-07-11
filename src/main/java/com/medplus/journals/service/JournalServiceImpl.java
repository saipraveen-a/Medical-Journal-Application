package com.medplus.journals.service;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.repository.CategoryRepository;
import com.medplus.journals.repository.JournalRepository;
import com.medplus.journals.repository.UserRepository;
import com.medplus.journals.translator.JournalTranslator;
import com.medplus.journals.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.medplus.journals.model.JournalEntity;
import com.medplus.journals.model.PublisherEntity;
import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.model.SubscriptionEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JournalServiceImpl implements JournalService {

	private final static Logger log = Logger.getLogger(JournalServiceImpl.class);

	@Autowired
	private JournalTranslator journalTranslator;

	@Autowired
	private JournalRepository journalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<JournalDTO> findJournalsSubscribedByUser(UserEntity user) {
		UserEntity persistentUser = userRepository.findOne(user.getId());
		List<SubscriptionEntity> subscriptions = persistentUser.getSubscriptions();
		if (subscriptions != null) {
			List<Long> ids = new ArrayList<>(subscriptions.size());
			subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
			return journalTranslator.translateTo(journalRepository.findByCategoryIdIn(ids));
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<JournalDTO> findJournalsByPublisher(PublisherEntity publisher) {
		Iterable<JournalEntity> journals = journalRepository.findByPublisher(publisher);
		List<JournalEntity> journalEntities = StreamSupport.stream(journals.spliterator(), false).collect(Collectors.toList());
		return journalTranslator.translateTo(journalEntities);
	}

	@Override
	public JournalDTO publishJournal(PublisherEntity publisher, JournalEntity journal, Long categoryId) throws ServiceException {
		CategoryEntity category = categoryRepository.findOne(categoryId);
		if(category == null) {
			throw new ServiceException("Category not found");
		}
		journal.setPublisher(publisher);
		journal.setCategory(category);
		try {
			return journalTranslator.translateTo(journalRepository.save(journal));
		} catch (DataIntegrityViolationException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void unPublish(PublisherEntity publisher, Long id) throws ServiceException {
		JournalEntity journal = journalRepository.findOne(id);
		if (journal == null) {
			throw new ServiceException("Journal doesn't exist");
		}
		String filePath = FileUtils.getFileName(publisher.getId(), journal.getUuid());
		File file = new File(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				log.error("File " + filePath + " cannot be deleted");
			}
		}
		if (!journal.getPublisher().getId().equals(publisher.getId())) {
			throw new ServiceException("Journal cannot be removed");
		}
		journalRepository.delete(journal);
	}

	@Override
	public List<JournalDTO> findUnNotifiedJournals() {
		List<JournalEntity> unNotifiedJournalEntities = journalRepository.findByNotified(false);

		return journalTranslator.translateTo(unNotifiedJournalEntities);
	}

	@Override
	public void updateJournalNotified(JournalDTO journal) {
		JournalEntity journalEntity = journalRepository.findOne(journal.getId());
		journalEntity.setNotified(true);
		journalRepository.save(journalEntity);
	}
}
