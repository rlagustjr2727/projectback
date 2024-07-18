package com.web.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.entity.NoticeEntity;

public interface NoticeService {
    List<NoticeEntity> getAllNotices();
    Page<NoticeEntity> getAllNotices(Pageable pageable);
    NoticeEntity getNoticeBySeq(Long seq);
    NoticeEntity saveNotice(NoticeEntity notice);
    NoticeEntity updateNotice(NoticeEntity notice);
    void deleteNotice(Long seq);
    void incrementViews(Long seq);
    Page<NoticeEntity> getNoticesByCategory(String category, Pageable pageable);
}
