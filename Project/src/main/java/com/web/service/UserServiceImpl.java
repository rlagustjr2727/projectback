package com.web.service;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.User;
import com.web.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User authenticateUser(String userId, String userPassword) {
        return userRepository.findByUserIdAndUserPassword(userId, userPassword);
    }
    
    @Override
    @Transactional
    public User getUserById(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            Hibernate.initialize(user.getFavorites());
        }
        return user;
    }
}