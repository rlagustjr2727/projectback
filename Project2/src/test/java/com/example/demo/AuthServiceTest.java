package com.example.demo;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.web.controller.AuthController;
import com.web.security.JwtTokenProvider;
import com.web.service.UserService;
import com.web.user.User;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testLoginSuccess() {
        User mockUser = new User();
        mockUser.setUserId("test");
        mockUser.setUserPassword("1234");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(userService.findByUserIdAndUserPassword("test", "1234")).thenReturn(mockUser);
        when(tokenProvider.generateToken("test")).thenReturn("testToken");

        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("userId", "test");
        loginRequest.put("userPassword", "1234");

        Map<String, String> response = authController.authenticateUser(loginRequest);

        assertEquals("testToken", response.get("token"));
        assertEquals("User authenticated successfully", response.get("message"));

        verify(userService, times(1)).findByUserIdAndUserPassword("test", "1234");
        verify(tokenProvider, times(1)).generateToken("test");
    }

    @Test
    public void testLoginFailure() {
        when(userService.findByUserIdAndUserPassword("invalidUser", "password")).thenReturn(null);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("userId", "invalidUser");
        loginRequest.put("userPassword", "password");

        Map<String, String> response = authController.authenticateUser(loginRequest);

        assertEquals("Invalid username or password", response.get("message"));

        verify(userService, times(1)).findByUserIdAndUserPassword("invalidUser", "password");
        verify(tokenProvider, never()).generateToken(anyString());
    }
}

