package com.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.entity.NoticeEntity;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long>, NoticeRepositoryCustom{

    Page<NoticeEntity> findByNoticeCategory(String category, Pageable pageable);
}
