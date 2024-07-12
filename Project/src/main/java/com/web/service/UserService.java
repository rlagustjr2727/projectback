package com.web.service;

import com.web.entity.User;

import java.util.Optional;

public interface UserService {
    boolean existsByUserId(String userId);
    boolean existsByUserNickName(String userNickName);
    void saveUser(User user);
    Optional<User> findByUserId(String userId);
}
