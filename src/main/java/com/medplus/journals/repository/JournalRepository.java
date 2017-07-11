package com.medplus.journals.repository;

import com.medplus.journals.model.JournalEntity;
import com.medplus.journals.model.PublisherEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface JournalRepository extends CrudRepository<JournalEntity, Long> {

    Collection<JournalEntity> findByPublisher(PublisherEntity publisher);

    List<JournalEntity> findByCategoryIdIn(List<Long> ids);

    List<JournalEntity> findByNotified(boolean notified);
}
