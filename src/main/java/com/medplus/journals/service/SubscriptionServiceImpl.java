package com.medplus.journals.service;

import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.repository.CategoryRepository;
import com.medplus.journals.repository.SubscriptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
  private final CategoryRepository categoryRepository;
  private final SubscriptionRepository subscriptionRepository;

  @Autowired
  public SubscriptionServiceImpl(CategoryRepository categoryRepository, SubscriptionRepository subscriptionRepository) {
    this.categoryRepository = categoryRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  @Override
  public List<SubscriptionEntity> findSubscriptionsByCategory(Long categoryId) {
    CategoryEntity categoryEntity = categoryRepository.findOne(categoryId);
    return subscriptionRepository.findByCategory(categoryEntity);
  }
}
