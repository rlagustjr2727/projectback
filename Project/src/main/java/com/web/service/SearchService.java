package com.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.Board;
import com.web.entity.Search;
import com.web.entity.Wboard;
import com.web.repository.BoardRepository;
import com.web.repository.SearchRepository;
import com.web.repository.WboardRepository;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;
    
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private WboardRepository wboardRepository;
    
    public Map<String, Object> search(String query) {
        Map<String, Object> result = new HashMap<>();
        
        // 게시판 검색 결과
        List<Board> boards = boardRepository.findByBoardTitleContainingOrBoardContentContaining(query, query);
        result.put("boards", boards);
        
        // 맞춤 추천 검색 결과
        List<Wboard> recommendations = wboardRepository.findByKeyword(query);
        result.put("recommendations", recommendations);
        
        return result;
    }


    public void saveSearchKeyword(String keyword) {
        Search search = new Search();
        search.setSearchKeyword(keyword);
        search.setSearchCount(1); // 초기 검색 횟수는 1
        search.setCreatedAt(new Date());
        searchRepository.save(search);
    }

    public List<Search> getPopularSearchTerms(int limit) {
        return searchRepository.findTopSearches(limit);
    }
}
