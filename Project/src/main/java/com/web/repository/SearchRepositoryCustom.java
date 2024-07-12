package com.web.repository;

import java.util.List;

import com.web.entity.Search;

public interface SearchRepositoryCustom {

	 List<Search> findTopSearchTerms(int limit);
}
