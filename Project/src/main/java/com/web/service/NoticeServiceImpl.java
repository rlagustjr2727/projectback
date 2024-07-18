package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.web.entity.NoticeEntity;
import com.web.repository.NoticeRepository;
import com.web.repository.NoticeRepositoryCustom;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    @Qualifier("noticeRepositoryCustomImpl")
    private NoticeRepositoryCustom noticeRepositoryCustom;
    
    @Override
    public List<NoticeEntity> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Override
    public Page<NoticeEntity> getAllNotices(Pageable pageable) {
        return noticeRepositoryCustom.findAllNotices(pageable);
    }

    @Override
    public NoticeEntity getNoticeBySeq(Long seq) {
        return noticeRepository.findById(seq).orElse(null);
    }

    @Override
    public NoticeEntity saveNotice(NoticeEntity notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public NoticeEntity updateNotice(NoticeEntity notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public void deleteNotice(Long seq) {
        noticeRepository.deleteById(seq);
    }
    
    @Override
    public void incrementViews(Long seq) {
        NoticeEntity notice = noticeRepository.findById(seq).orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setNoticeViewCount(notice.getNoticeViewCount() + 1);
        noticeRepository.save(notice);
    }
    
    @Override
    public Page<NoticeEntity> getNoticesByCategory(String category, Pageable pageable) {
        return noticeRepository.findByNoticeCategory(category, pageable);
    }
}

