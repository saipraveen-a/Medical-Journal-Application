package com.medplus.journals.repository;

import com.medplus.journals.model.PublisherEntity;
import com.medplus.journals.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    Optional<PublisherEntity> findByUser(UserEntity user);

}
