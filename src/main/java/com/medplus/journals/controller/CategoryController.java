package com.medplus.journals.controller;

import com.medplus.journals.dto.SubscriptionDTO;
import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.repository.CategoryRepository;

import com.medplus.journals.service.CurrentUser;
import com.medplus.journals.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private UserService userService;


  @RequestMapping(value = "")
  public List<CategoryEntity> getCategories() {
    return categoryRepository.findAll();
  }

  @RequestMapping(value = "/subscriptions")
  public List<SubscriptionDTO> getUserSubscriptions(@AuthenticationPrincipal CurrentUser activeUser) {
    UserEntity persistedUser = userService.findById(activeUser.getId());
    List<SubscriptionEntity> subscriptions = persistedUser.getSubscriptions();
    List<CategoryEntity> categories = categoryRepository.findAll();
    List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>(categories.size());
    categories.stream().forEach(c -> {
      SubscriptionDTO subscr = new SubscriptionDTO(c);
      Optional<SubscriptionEntity> subscription = subscriptions.stream().filter(s -> s.getCategory().getId().equals(c.getId())).findFirst();
      subscr.setActive(subscription.isPresent());
      subscriptionDTOs.add(subscr);
    });
    return subscriptionDTOs;
  }

  @RequestMapping(value = "/{categoryId}/subscribe", method = RequestMethod.POST)
  public void subscribe(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal CurrentUser activeUser) {
    UserEntity user = userService.findById(activeUser.getUser().getId());
    userService.subscribe(user, categoryId);
  }
}
