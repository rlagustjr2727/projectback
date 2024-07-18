package com.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.entity.Search;
import com.web.service.BoardService;
import com.web.service.SearchService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> search(@RequestParam String query) {
        searchService.saveSearchKeyword(query);
        Map<String, Object> result = searchService.search(query);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Search>> getPopularSearchTerms(@RequestParam int limit) {
        return ResponseEntity.ok(searchService.getPopularSearchTerms(limit));
    }
}
