package com.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.entity.User;
import com.web.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 사용

    @Override
    public void registerUser(User user) {
        String rawPassword = user.getUserPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setUserPassword(encodedPassword);
        logger.info("Raw password for user {}: {}", user.getUserId(), rawPassword);
        logger.info("Encoded password for user {}: {}", user.getUserId(), encodedPassword);
        userRepository.save(user);
    }

    @Override
    public User findByUserIdAndUserPassword(String userId, String userPassword) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            boolean matches = passwordEncoder.matches(userPassword, user.getUserPassword());
            logger.info("Password matches for user {}: {}", userId, matches);
            if (matches) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickName(userNickName);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
