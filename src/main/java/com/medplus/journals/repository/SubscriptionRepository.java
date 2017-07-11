package com.medplus.journals.repository;

import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.SubscriptionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
  List<SubscriptionEntity> findByCategory(CategoryEntity category);
}
