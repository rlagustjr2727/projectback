package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.repository.UserRepository;
import com.web.service.UserService;
import com.web.user.User;

@SpringBootTest
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testRegisterUser() {
        User user = new User();
        user.setUserId("testuser");
        user.setUserName("John Doe");
        user.setUserNickName("johndoe");
        user.setUserPassword("password123");
        user.setUserEmail("john.doe@example.com");
        user.setUserDomain("example.com");
        user.setUserBirth(new Date());
        user.setUserPhoneNum("123-456-7890");
        user.setUserProfileImage("profile.jpg");

        userService.registerUser(user);

        User foundUser = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(foundUser.getUserNickName()).isEqualTo(user.getUserNickName());
        assertThat(foundUser.getUserPassword()).isEqualTo(user.getUserPassword());
        assertThat(foundUser.getUserEmail()).isEqualTo(user.getUserEmail());
        assertThat(foundUser.getUserDomain()).isEqualTo(user.getUserDomain());
        assertThat(foundUser.getUserBirth()).isEqualTo(user.getUserBirth());
        assertThat(foundUser.getUserPhoneNum()).isEqualTo(user.getUserPhoneNum());
        assertThat(foundUser.getUserProfileImage()).isEqualTo(user.getUserProfileImage());
    }
}
