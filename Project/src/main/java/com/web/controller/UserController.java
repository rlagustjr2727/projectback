package com.web.controller;

import com.web.entity.User;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.existsByUserId(user.getUserId())) {
            return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
        }

        if (userService.existsByUserNickName(user.getUserNickName())) {
            return ResponseEntity.badRequest().body("닉네임이 이미 존재합니다.");
        }

        userService.saveUser(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/checkUserId")
    public ResponseEntity<Boolean> checkUserId(@RequestParam String userId) {
        boolean exists = userService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/checkUserNickName")
    public ResponseEntity<Boolean> checkUserNickName(@RequestParam String userNickName) {
        boolean exists = userService.existsByUserNickName(userNickName);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpSession session) {
        User user = userService.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!loginRequest.getUserPassword().equals(user.getUserPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("user", user);
        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserId(@PathVariable String userId) {
        User user = userService.findByUserId(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }
}
