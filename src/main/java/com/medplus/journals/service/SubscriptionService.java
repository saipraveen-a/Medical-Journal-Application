package com.medplus.journals.service;

import com.medplus.journals.model.SubscriptionEntity;

import java.util.List;

public interface SubscriptionService {
  List<SubscriptionEntity> findSubscriptionsByCategory(Long categoryId);
}
