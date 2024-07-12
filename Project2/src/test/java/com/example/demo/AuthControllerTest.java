package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.controller.AuthController;
import com.web.security.JwtTokenProvider;
import com.web.service.UserService;
import com.web.user.User;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserDetailsService userDetailsService; // Mock the UserDetailsService

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("testUser");
        user.setUserPassword("plainPassword");
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenCredentialsAreCorrect() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("userId", "testUser");
        loginRequest.put("userPassword", "plainPassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "plainPassword");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("testToken");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUser_ShouldReturnError_WhenCredentialsAreIncorrect() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("userId", "testUser");
        loginRequest.put("userPassword", "plainPassword");

        when(authenticationManager.authenticate(any())).thenThrow(AuthenticationException.class);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_ShouldReturnOk_WhenUserIsRegisteredSuccessfully() throws Exception {
        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("userId", "testUser");
        userRequest.put("userName", "Test User");
        userRequest.put("userPassword", "plainPassword");
        userRequest.put("userEmail", "test@example.com");
        userRequest.put("userDomain", "example.com");

        when(userService.isUserIdExists(any())).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenUserIdIsMissing() throws Exception {
        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("userName", "Test User");
        userRequest.put("userPassword", "plainPassword");
        userRequest.put("userEmail", "test@example.com");
        userRequest.put("userDomain", "example.com");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }
}
