package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.web.entity.Search;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom{

	 @Query("SELECT s FROM Search s ORDER BY s.searchCount DESC")
	    List<Search> findTopSearches(int limit);
	 
	 
}
