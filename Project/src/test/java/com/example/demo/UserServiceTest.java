package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.web.repository.UserRepository;
import com.web.service.UserServiceImpl;
import com.web.user.User;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setUserId("testUser");
        user.setUserPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn(passwordEncoder.encode("plainPassword"));

        userService.registerUser(user);

        assertNotNull(user.getUserPassword());
        assertTrue(passwordEncoder.matches("plainPassword", user.getUserPassword()));
    }

    @Test
    public void testFindByUserIdAndUserPassword_Success() {
        User user = new User();
        user.setUserId("testUser");
        user.setUserPassword(passwordEncoder.encode("plainPassword"));

        when(userRepository.findByUserId(anyString())).thenReturn(user);
        when(passwordEncoder.matches("plainPassword", user.getUserPassword())).thenReturn(true);

        User foundUser = userService.findByUserIdAndUserPassword("testUser", "plainPassword");

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUserId());
    }

    @Test
    public void testFindByUserIdAndUserPassword_Failure() {
        User user = new User();
        user.setUserId("testUser");
        user.setUserPassword(passwordEncoder.encode("plainPassword"));

        when(userRepository.findByUserId(anyString())).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getUserPassword())).thenReturn(false);

        User foundUser = userService.findByUserIdAndUserPassword("testUser", "wrongPassword");

        assertNull(foundUser);
    }
}
