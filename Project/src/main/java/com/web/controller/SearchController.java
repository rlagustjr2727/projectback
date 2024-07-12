package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.entity.Board;
import com.web.entity.Search;
import com.web.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

    @GetMapping
    public List<Search> search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        return searchService.search(keyword);
    }

    @GetMapping("/popular")
    public List<Search> getPopularSearchTerms(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return searchService.getPopularSearchTerms(limit);
    }
}
