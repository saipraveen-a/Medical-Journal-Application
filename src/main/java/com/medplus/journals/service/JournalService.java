package com.medplus.journals.service;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.model.JournalEntity;
import com.medplus.journals.model.PublisherEntity;
import com.medplus.journals.model.UserEntity;

import java.util.List;

public interface JournalService {

	List<JournalDTO> findJournalsSubscribedByUser(UserEntity user);

	List<JournalDTO> findJournalsByPublisher(PublisherEntity publisher);

	JournalDTO publishJournal(PublisherEntity publisher, JournalEntity journal, Long categoryId);

	void unPublish(PublisherEntity publisher, Long journalId);

	List<JournalDTO> findUnNotifiedJournals();

	void updateJournalNotified(JournalDTO journal);
}
