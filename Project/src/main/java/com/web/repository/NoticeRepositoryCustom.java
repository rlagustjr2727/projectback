package com.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.entity.NoticeEntity;



public interface NoticeRepositoryCustom {
	 Page<NoticeEntity> findAllNotices(Pageable pageable);
	 Page<NoticeEntity> findByNoticeCategory(String category, Pageable pageable);
    // 필요한 다른 커스텀 메서드 선언
}