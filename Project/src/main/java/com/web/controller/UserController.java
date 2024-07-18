// UserController.java
package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            // 이미지 파일을 서버에 저장하는 로직을 추가합니다.
            String imageUrl = saveImageToFileSystem(image);
            
            // 데이터베이스에서 사용자 프로필 이미지 URL을 업데이트합니다.
            User updatedUser = userService.updateProfileImage(user.getUserId(), imageUrl);
            
            // 세션에 업데이트된 사용자 정보를 저장합니다.
            session.setAttribute("user", updatedUser);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    private String saveImageToFileSystem(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path path = Paths.get(UPLOADED_FOLDER, fileName);
        Files.createDirectories(path.getParent());
        Files.copy(image.getInputStream(), path);
        return "/" + UPLOADED_FOLDER + fileName;
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
