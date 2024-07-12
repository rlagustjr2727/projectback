package com.web.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.web.entity.Search;

public class SearchRepositoryCustomImpl implements SearchRepositoryCustom{

	   @PersistenceContext
	    private EntityManager entityManager;

	    @Override
	    public List<Search> findTopSearchTerms(int limit) {
	        String sql = "SELECT s FROM Search s ORDER BY s.searchCount DESC";
	        Query query = entityManager.createQuery(sql, Search.class);
	        query.setMaxResults(limit);
	        return query.getResultList();
	    }
}
