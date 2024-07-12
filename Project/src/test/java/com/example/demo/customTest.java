package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.repository.UserRepository;

@SpringBootTest

public class customTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testExistsByUserIdCustom() {
        boolean exists = userRepository.existsByUserIdCustom("testUserId");
        assertNotNull(exists);
    }

    @Test
    public void testExistsByUserNickNameCustom() {
        boolean exists = userRepository.existsByUserNickNameCustom("testUserNickName");
        assertNotNull(exists);
    }
}
