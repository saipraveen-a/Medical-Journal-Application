package com.medplus.journals.service;

import com.medplus.journals.model.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public CurrentUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public CurrentUser loadUserByUsername(String loginName) throws UsernameNotFoundException {
		UserEntity user = userService.getUserByLoginName(loginName).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", loginName)));
		return new CurrentUser(user);
	}

}
