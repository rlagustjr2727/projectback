package com.web.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Bean(name = "googleMailSender")
	public JavaMailSender googleMailSender () {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("seungjaebaekmail@gmail.com");
		mailSender.setPassword("efycacxbbodaaved");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.startls.required", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		return mailSender;
	}
	
	@Bean(name = "naverMailSender")
	public JavaMailSender naverMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.naver.com");
		mailSender.setPort(465);
		mailSender.setUsername("baekseungjae1994@naver.com");
		mailSender.setPassword("baekseungjae");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.trust", "smtp.naver.com");
		
		return mailSender;
	}
	
}
