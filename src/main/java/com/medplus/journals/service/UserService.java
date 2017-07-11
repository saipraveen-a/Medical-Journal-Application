package com.medplus.journals.service;

import com.medplus.journals.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserEntity> getUserByLoginName(String loginName);

    void subscribe(UserEntity user, Long categoryId);

    UserEntity findById(Long id);

    List<UserEntity> getAllUsers();
}