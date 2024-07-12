package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.dto.EventDTO;
import com.web.service.NaverSearchService;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private NaverSearchService naverSearchService;

    @GetMapping("/whisky")
    public ResponseEntity<List<EventDTO>> getWhiskyEvents() {
        try {
            List<EventDTO> events = naverSearchService.getWhiskyEvents("위스키 행사");
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 상태 반환
        }
    }
}
