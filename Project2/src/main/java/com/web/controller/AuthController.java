package com.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.entity.User;
import com.web.repository.UserRepositoryCustom;
import com.web.security.CustomUserDetailsService;
import com.web.security.JwtTokenProvider;
import com.web.security.UserPrincipal;
import com.web.service.EmailService;
import com.web.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 사용
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    @Qualifier("userRepositoryCustomImpl")
    private UserRepositoryCustom userRepositoryCustom;
    
    @Autowired // EmailService를 자동 주입
    private EmailService emailService; 

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            String userId = loginRequest.get("userId");
            String userPassword = loginRequest.get("userPassword");

            logger.info("Received userId: {}", userId);
            logger.info("Received password: {}", userPassword != null ? "****" : "null");

            if (userId == null || userId.isEmpty() || userPassword == null || userPassword.isEmpty()) {
                response.put("message", "User ID and Password cannot be null");
                logger.error("User ID and/or Password cannot be null");
                return response;
            }

            logger.info("Attempting authentication for userId: {}", userId);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

            logger.info("Loaded userDetails: {}", userDetails);

            String encodedPassword = userDetails.getPassword();
            logger.info("Encoded password from database: {}", encodedPassword);
            logger.info("Raw password from request: {}", userPassword);

            boolean passwordMatches = passwordEncoder.matches(userPassword, encodedPassword);
            logger.info("Password matches for userId {}: {}", userId, passwordMatches);

            if (passwordMatches) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userId, userPassword));

                String token = tokenProvider.generateToken(authentication);

                response.put("token", token);
                response.put("message", "Login success");
                logger.info("Authentication successful for userId: {}", userId);
            } else {
                response.put("message", "Invalid username or password");
                logger.error("Authentication failed for userId: {}", userId);
            }
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for userId: {}", loginRequest.get("userId"), e);
            response.put("message", "Invalid username or password");
        } catch (Exception e) {
            logger.error("An error occurred during authentication", e);
            response.put("message", "An error occurred: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userRequest) throws ParseException {
        String userId = userRequest.get("userId");
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserName(userRequest.get("userName"));
        user.setUserNickName(userRequest.get("userNickName"));
        user.setUserPassword(passwordEncoder.encode(userRequest.get("userPassword")));  // 비밀번호 인코딩
        user.setUserEmail(userRequest.get("userEmail"));
        user.setUserDomain(userRequest.get("userDomain"));

        String userBirthStr = userRequest.get("userBirth");
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date userBirth = dataFormat.parse(userBirthStr);
        user.setUserBirth(userBirth);

        user.setUserPhoneNum(userRequest.get("userPhoneNum"));
        user.setUserProfileImage(userRequest.getOrDefault("userProfileImage", ""));

        if (!emailService.isVerified(user.getUserEmail())) {
            return ResponseEntity.badRequest().body("이메일 인증을 완료해야합니다.");
        }

        userService.registerUser(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/checkVerificationStatus")
    public Map<String, Boolean> checkVerificationStatus(@RequestBody Map<String, String> request) {
        // Email verification logic here
        Map<String, Boolean> response = new HashMap<>();
        // For demonstration, let's assume email is verified
        response.put("isVerified", true);
        return response;
    }

    @PostMapping("/sendVerificationCode") // 인증 코드 전송 요청을 처리
    public Map<String, String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String provider = request.get("provider"); // 이메일 제공자
        emailService.sendVerificationCode(email, provider); // 인증 코드 전송
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "인증번호가 이메일로 전송되었습니다.");
        return response;
    }
    @GetMapping("/user/{userId}") // 사용자 정보 요청을 처리
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user); // 사용자 정보 반환
        } else {
            return ResponseEntity.notFound().build(); // 사용자 정보가 없을 경우 404 응답
        }
    }
    
    // 로그아웃 엔드 포인트를 구현, 기본적으로 로그아웃 요청을 처리하고, 세션을 무효화하는 역할을 합니다.
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 상태 코드 설정
    }

    @PostMapping("/verifyCode")
    public Map<String, Boolean> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        boolean isValid = emailService.verifyCode(email, code); // 인증 코드 확인
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        return response;
    }
    
    @GetMapping("/checkUserId")
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userRepositoryCustom.existsByUserIdCustom(userId));
        return response;
    }

    @GetMapping("/checkUserNickName")
    public Map<String, Boolean> checkUserNickName(@RequestParam String userNickName) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userRepositoryCustom.existsByUserNickNameCustom(userNickName));
        return response;
    }
    
    @GetMapping("/user")
    public ResponseEntity<UserPrincipal> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userPrincipal);
    }
}
