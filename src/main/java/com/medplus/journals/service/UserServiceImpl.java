package com.medplus.journals.service;

import com.medplus.journals.model.CategoryEntity;
import com.medplus.journals.model.UserEntity;
import com.medplus.journals.repository.CategoryRepository;
import com.medplus.journals.model.SubscriptionEntity;
import com.medplus.journals.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<UserEntity> getUserByLoginName(String loginName) {
		return Optional.ofNullable(userRepository.findByLoginName(loginName));
	}

	@Override
	public void subscribe(UserEntity user, Long categoryId) {
		List<SubscriptionEntity> subscriptions;
		subscriptions = user.getSubscriptions();
		if (subscriptions == null) {
			subscriptions = new ArrayList<>();
		}
		Optional<SubscriptionEntity> subscr = subscriptions.stream()
				.filter(s -> s.getCategory().getId().equals(categoryId)).findFirst();
		if (!subscr.isPresent()) {
			SubscriptionEntity s = new SubscriptionEntity();
			s.setUser(user);
			CategoryEntity category = categoryRepository.findOne(categoryId);
			if(category == null) {
				throw new ServiceException("Category not found");
			}
			s.setCategory(category);
			subscriptions.add(s);
			userRepository.save(user);
		}
	}

	@Override
	public UserEntity findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
}
