package com.web.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.NoticeEntity;
import com.web.repository.NoticeRepository;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<NoticeEntity> getAllNotice() {
        return noticeRepository.findAll();
    }

    public Page<NoticeEntity> getAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    public Optional<NoticeEntity> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    public NoticeEntity createNotice(NoticeEntity notice, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            notice.setNoticeContentImage(fileName); // 파일 이름을 데이터베이스에 저장
            // 파일 저장 로직 추가 (예: storageService.store(file));
        }
        notice.setNoticeDateTime(new Date()); // 현재 시간을 공지 생성 시간으로 설정
        return noticeRepository.save(notice);
    }

    public NoticeEntity updateNotice(Long id, NoticeEntity noticeDetails, MultipartFile file) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id " + id));

        notice.setAdminId(noticeDetails.getAdminId());
        notice.setNoticeTitle(noticeDetails.getNoticeTitle());
        notice.setNoticeContent(noticeDetails.getNoticeContent());

        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            notice.setNoticeContentImage(fileName); // 파일 이름을 데이터베이스에 저장
            // 파일 저장 로직 추가
        }

        notice.setNoticeDateTime(new Date()); // 현재 시간으로 업데이트 시간 설정

        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}