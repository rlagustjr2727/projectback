package com.web.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.web.entity.Board;



@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<Board> findAllBoards(Pageable pageable) {
		// Total count query
		String countQueryStr = "SELECT COUNT(*) FROM board";
		Query countQuery = entityManager.createNativeQuery(countQueryStr);
		long count = ((Number) countQuery.getSingleResult()).longValue();

		// Data query with pagination and order by board_date desc
		String queryStr = "SELECT * FROM (SELECT b.*, ROWNUM rnum FROM (SELECT * FROM board ORDER BY board_date DESC) b WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
		Query query = entityManager.createNativeQuery(queryStr, Board.class);
		query.setParameter("startRow", pageable.getOffset());
		query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

		List<Board> boards = query.getResultList();

		return new PageImpl<>(boards, pageable, count);
	}

	// 제목으로 검색을 위한 커스텀 메서드 추가
	@Override
	public Page<Board> searchBoardsByTitle(String title, Pageable pageable) {
		// Total count query with title filter
		String countQueryStr = "SELECT COUNT(*) FROM board WHERE board_title LIKE :title";
		Query countQuery = entityManager.createNativeQuery(countQueryStr);
		countQuery.setParameter("title", "%" + title + "%");
		long count = ((Number) countQuery.getSingleResult()).longValue();

		// Data query with pagination, title filter, and order by board_date desc
		String queryStr = "SELECT * FROM (SELECT b.*, ROWNUM rnum FROM (SELECT * FROM board WHERE board_title LIKE :title ORDER BY board_date DESC) b WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
		Query query = entityManager.createNativeQuery(queryStr, Board.class);
		query.setParameter("title", "%" + title + "%");
		query.setParameter("startRow", pageable.getOffset());
		query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

		List<Board> boards = query.getResultList();

		return new PageImpl<>(boards, pageable, count);
	}
	
	@Override
    public Page<Board> findBoardsByCategory(String category, Pageable pageable) {
        // Total count query with category filter
        String countQueryStr = "SELECT COUNT(*) FROM board WHERE board_category = :category";
        Query countQuery = entityManager.createNativeQuery(countQueryStr);
        countQuery.setParameter("category", category);
        long count = ((Number) countQuery.getSingleResult()).longValue();

        // Data query with pagination, category filter, and order by board_date desc
        String queryStr = "SELECT * FROM (SELECT b.*, ROWNUM rnum FROM (SELECT * FROM board WHERE board_category = :category ORDER BY board_date DESC) b WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
        Query query = entityManager.createNativeQuery(queryStr, Board.class);
        query.setParameter("category", category);
        query.setParameter("startRow", pageable.getOffset());
        query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

        List<Board> boards = query.getResultList();

        return new PageImpl<>(boards, pageable, count);
    }

	  @Override
	    public Page<Board> findRecentBoards(Pageable pageable) {
	        // Total count query
	        String countQueryStr = "SELECT COUNT(*) FROM board";
	        Query countQuery = entityManager.createNativeQuery(countQueryStr);
	        long count = ((Number) countQuery.getSingleResult()).longValue();

	        // Data query with pagination and order by board_date desc
	        String queryStr = "SELECT * FROM (SELECT b.*, ROWNUM rnum FROM (SELECT * FROM board ORDER BY board_date DESC) b WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
	        Query query = entityManager.createNativeQuery(queryStr, Board.class);
	        query.setParameter("startRow", pageable.getOffset());
	        query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

	        List<Board> boards = query.getResultList();

	        return new PageImpl<>(boards, pageable, count);
	    }
}