package com.medplus.journals.repository;

import com.medplus.journals.model.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.medplus.journals.model.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLoginName(String loginName);

    List<UserEntity> findBySubscriptions(List<SubscriptionEntity> subscriptions);
}
