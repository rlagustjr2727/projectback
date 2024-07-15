package com.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	
    private String userId; // 유저 ID를 저장합니다.
    private String userPassword; // 유저 비밀번호를 저장합니다.
    
}
