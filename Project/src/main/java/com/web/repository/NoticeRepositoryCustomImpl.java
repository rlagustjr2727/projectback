package com.web.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.web.notice.NoticeEntity;

@Repository
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

	@Autowired
    private EntityManager entityManager;

	@Override
	public Page<NoticeEntity> searchNoticesByTitle(String keyword, Pageable pageable) {
	    // Total count query with title filter
	    String countQueryStr = "SELECT COUNT(*) FROM notice WHERE notice_title LIKE :keyword";
	    Query countQuery = entityManager.createNativeQuery(countQueryStr);
	    countQuery.setParameter("keyword", "%" + keyword + "%");
	    long count = ((Number) countQuery.getSingleResult()).longValue();

	    // Data query with pagination, title filter, and order by notice_date desc
	    String queryStr = "SELECT * FROM notice WHERE notice_title LIKE :keyword ORDER BY notice_date DESC LIMIT :limit OFFSET :offset";
	    Query query = entityManager.createNativeQuery(queryStr, NoticeEntity.class);
	    query.setParameter("keyword", "%" + keyword + "%");
	    query.setParameter("offset", pageable.getOffset());
	    query.setParameter("limit", pageable.getPageSize());

	    List<NoticeEntity> notices = query.getResultList();

	    return new PageImpl<>(notices, pageable, count);
	}

}