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
import com.web.entity.NoticeEntity;

@Repository
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<NoticeEntity> findAllNotices(Pageable pageable) {
        // Total count query
        String countQueryStr = "SELECT COUNT(*) FROM notice";
        Query countQuery = entityManager.createNativeQuery(countQueryStr);
        long count = ((Number) countQuery.getSingleResult()).longValue();

        // Data query with pagination and order by notice_date_time desc
        String queryStr = "SELECT * FROM (SELECT n.*, ROWNUM rnum FROM (SELECT * FROM notice ORDER BY notice_date_time DESC) n WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
        Query query = entityManager.createNativeQuery(queryStr, NoticeEntity.class);
        query.setParameter("startRow", pageable.getOffset());
        query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

        List<NoticeEntity> notices = query.getResultList();

        return new PageImpl<>(notices, pageable, count);
    }
    
    @Override
    public Page<NoticeEntity> findByNoticeCategory(String category, Pageable pageable) {
        // Total count query with category filter
        String countQueryStr = "SELECT COUNT(*) FROM notice WHERE notice_category = :category";
        Query countQuery = entityManager.createNativeQuery(countQueryStr);
        countQuery.setParameter("category", category);
        long count = ((Number) countQuery.getSingleResult()).longValue();

        // Data query with pagination, category filter, and order by notice_date_time desc
        String queryStr = "SELECT * FROM (SELECT n.*, ROWNUM rnum FROM (SELECT * FROM notice WHERE notice_category = :category ORDER BY notice_date_time DESC) n WHERE ROWNUM <= :endRow) WHERE rnum > :startRow";
        Query query = entityManager.createNativeQuery(queryStr, NoticeEntity.class);
        query.setParameter("category", category);
        query.setParameter("startRow", pageable.getOffset());
        query.setParameter("endRow", pageable.getOffset() + pageable.getPageSize());

        List<NoticeEntity> notices = query.getResultList();

        return new PageImpl<>(notices, pageable, count);
    }
}
