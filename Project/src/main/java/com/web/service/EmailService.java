package com.web.service; // 패키지 선언

import java.util.Random; // Random 클래스를 임포트
import java.util.concurrent.ConcurrentHashMap; // ConcurrentHashMap 클래스를 임포트

import javax.mail.MessagingException; // MessagingException 클래스를 임포트
import javax.mail.internet.MimeMessage; // MimeMessage 클래스를 임포트

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.beans.factory.annotation.Qualifier; // Qualifier 어노테이션을 임포트
import org.springframework.mail.MailException; // MailException 클래스를 임포트
import org.springframework.mail.javamail.JavaMailSender; // JavaMailSender 인터페이스를 임포트
import org.springframework.mail.javamail.MimeMessageHelper; // MimeMessageHelper 클래스를 임포트
import org.springframework.stereotype.Service; // Service 어노테이션을 임포트

@Service
public class EmailService {

    @Autowired
    @Qualifier("googleMailSender")
    private JavaMailSender googleMailSender;

    @Autowired
    @Qualifier("naverMailSender")
    private JavaMailSender naverMailSender;

    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> verificationStatus = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String sendVerificationCode(String to, String provider) {
        String code = generateVerificationCode();
        try {
            MimeMessage mimeMessage = getMailSender(provider).createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String htmlMsg = "<h3>이메일 인증 코드</h3>"
                    + "<p>안녕하세요.</p>"
                    + "<p>다음은 귀하의 이메일 인증 코드입니다. <strong>" + code + "</strong></p>"
                    + "<p>이 코드를 사용하여 이메일 인증을 완료하세요.</p>"
                    + "<p>감사합니다.</p>";
            helper.setText(htmlMsg, true);
            helper.setTo(to);
            helper.setSubject("이메일 인증 코드");
            helper.setFrom(getSenderEmail(provider));
            getMailSender(provider).send(mimeMessage);
            verificationCodes.put(to, code); // 이메일과 코드 매핑
            verificationStatus.put(to, false); // 초기 인증 상태는 false
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("인증 코드 발송 실패", e);
        }
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String correctCode = verificationCodes.get(email);
        if (correctCode != null && correctCode.equals(code)) {
            verificationStatus.put(email, true); // 이메일 인증 성공 시 상태를 true 로 설정
            return true;
        }
        return false;
    }

    public boolean isVerified(String email) {
        return verificationStatus.getOrDefault(email, false); // 이메일 인증 여부 확인
    }

    private String generateVerificationCode() {
        int code = random.nextInt(9000) + 1000; // 1000-9999 사이의 숫자 생성
        return String.format("%04d", code);
    }

    private JavaMailSender getMailSender(String provider) {
        if ("google".equalsIgnoreCase(provider)) {
            return googleMailSender;
        } else if ("naver".equalsIgnoreCase(provider)) {
            return naverMailSender;
        } else {
            throw new IllegalArgumentException("지원하지 않는 이메일 제공자입니다.: " + provider);
        }
    }

    private String getSenderEmail(String provider) {
        if ("google".equalsIgnoreCase(provider)) {
            return "seungjaebaekmail@gmail.com";
        } else if ("naver".equalsIgnoreCase(provider)) {
            return "baekseungjae1994@naver.com";
        } else {
            throw new IllegalArgumentException("지원하지 않는 이메일 제공자입니다. " + provider);
        }
    }
}
