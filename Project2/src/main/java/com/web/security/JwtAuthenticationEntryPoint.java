package com.web.security; // 패키지 선언

import org.springframework.security.core.AuthenticationException; // AuthenticationException 클래스를 임포트
import org.springframework.security.web.AuthenticationEntryPoint; // AuthenticationEntryPoint 인터페이스를 임포트
import org.springframework.stereotype.Component; // Component 어노테이션을 임포트

import javax.servlet.http.HttpServletRequest; // HttpServletRequest 클래스를 임포트
import javax.servlet.http.HttpServletResponse; // HttpServletResponse 클래스를 임포트
import java.io.IOException; // IOException 클래스를 임포트

@Component // 이 클래스를 Spring Bean으로 등록
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // AuthenticationEntryPoint 인터페이스를 구현하는 클래스 선언

    @Override // AuthenticationEntryPoint 인터페이스의 메소드 구현을 나타냄
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // 인증되지 않은 요청에 대해 401 에러 반환
    }
}
