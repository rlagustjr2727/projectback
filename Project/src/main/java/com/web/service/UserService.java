package com.web.service;

import com.web.entity.User;

public interface UserService {
    void registerUser(User user); // 유저 정보를 등록합니다.
    User authenticateUser(String userId, String userPassword); // 유저 인증을 수행합니다.
	User updateUser(User user);
    User getUserById(String userId);
    User updateProfileImage(String userId, String imageUrl);
}
