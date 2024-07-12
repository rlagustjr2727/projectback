package com.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private String title;
    private String description;
    private String link;
    private String imageUrl; // 이미지 URL 필드 추가

    // 기본 생성자
    public EventDTO() {}

    // 매개변수 생성자
    public EventDTO(String title, String description, String link, String imageUrl) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
    }

}
