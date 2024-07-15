package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.Search;
import com.web.repository.SearchRepository;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public List<Search> search(String keyword) {
        saveSearchTerm(keyword);
        return searchRepository.findBySearchKeywordContainingIgnoreCase(keyword);
    }

    public Search saveSearch(Search search) {
        return searchRepository.save(search);
    }

    public void saveSearchTerm(String keyword) {
        List<Search> existingSearches = searchRepository.findBySearchKeywordContainingIgnoreCase(keyword);
        if (existingSearches.isEmpty()) {
            Search newSearch = new Search();
            newSearch.setSearchKeyword(keyword);
            newSearch.setSearchCount(1);
            searchRepository.save(newSearch);
        } else {
            Search existingSearch = existingSearches.get(0);
            existingSearch.setSearchCount(existingSearch.getSearchCount() + 1);
            searchRepository.save(existingSearch);
        }
    }

    public List<Search> getPopularSearchTerms(int limit) {
        return searchRepository.findTopSearchTerms(limit);
    }
}
