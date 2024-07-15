// UserController.java
package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.User;
import com.web.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    private static final String UPLOADED_FOLDER = "uploads/";
    
    
    // 회원가입 메서드
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // 프로필 이미지 업데이트 메서드
    @PostMapping("/update-profile-image")
    public ResponseEntity<User> updateProfileImage(@RequestParam("image") MultipartFile image, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            byte[] bytes = image.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());
            
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            
            Files.write(path, bytes);
            user.setUserProfileImage(UPLOADED_FOLDER + image.getOriginalFilename());
            userService.updateUser(user);
            session.setAttribute("user", user); // 세션 업데이트
            return ResponseEntity.ok(user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    // 회원정보 수정 메서드
    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(403).build();
        }

        currentUser.setUserNickName(user.getUserNickName());
        currentUser.setUserEmail(user.getUserEmail());
        currentUser.setUserPhoneNum(user.getUserPhoneNum());
        currentUser.setUserPassword(user.getUserPassword());

        userService.updateUser(currentUser);
        session.setAttribute("user", currentUser);

        return ResponseEntity.ok(currentUser);
    }
}
