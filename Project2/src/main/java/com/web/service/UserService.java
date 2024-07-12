package com.web.service;

import com.web.entity.User;

public interface UserService {
    void registerUser(User user);
    User findByUserIdAndUserPassword(String userId, String userPassword);
    boolean isUserIdExists(String userId);
    boolean isUserNickNameExists(String userNickName);
    User getUserById(String userId);
}
