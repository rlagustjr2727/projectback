package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.web.service.EmailService;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendVerificationCode() {
        String email = "your_test_email@gmail.com";
        String provider = "google";
        emailService.sendVerificationCode(email, provider);
        // 추가로 이메일 수신 여부를 확인하는 코드를 작성할 수 있습니다.
    }
}
