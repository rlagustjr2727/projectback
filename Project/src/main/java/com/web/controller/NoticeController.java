package com.web.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.entity.NoticeEntity;
import com.web.entity.User;
import com.web.service.NoticeService;

@RestController
@RequestMapping("/api/notice")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    private static final String UPLOADED_FOLDER = "uploads/";

    @GetMapping("/all")
    public List<NoticeEntity> getAllNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping
    public Page<NoticeEntity> getAllNotices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return noticeService.getAllNotices(PageRequest.of(page, size));
    }

    @GetMapping("/{seq}")
    public NoticeEntity getNoticeBySeq(@PathVariable Long seq) {
        return noticeService.getNoticeBySeq(seq);
    }

    @PostMapping
    public ResponseEntity<NoticeEntity> createNotice(
            @RequestPart("notice") NoticeEntity notice,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);
                notice.setNoticeContentImage(UPLOADED_FOLDER + image.getOriginalFilename());
            } else {
                notice.setNoticeContentImage(null);
            }
            notice.setNoticeDateTime(new Date());
            notice.setAdminId(user.getUserNickName());

            if (notice.getNoticeTitle() == null || notice.getNoticeTitle().trim().isEmpty()) {
                return ResponseEntity.status(400).body(null); // 제목이 비어있으면 400 에러 반환
            }

            NoticeEntity savedNotice = noticeService.saveNotice(notice);
            return ResponseEntity.ok(savedNotice);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{seq}")
    public ResponseEntity<NoticeEntity> updateNotice(
            @PathVariable Long seq,
            @RequestPart("notice") MultipartFile noticePart,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            String noticeJson = new String(noticePart.getBytes(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            NoticeEntity notice = objectMapper.readValue(noticeJson, NoticeEntity.class);

            NoticeEntity existingNotice = noticeService.getNoticeBySeq(seq);

            if (!existingNotice.getAdminId().equals(user.getUserNickName())) {
                return ResponseEntity.status(403).build();
            }

            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);
                existingNotice.setNoticeContentImage(UPLOADED_FOLDER + image.getOriginalFilename());
            }

            if (notice.getNoticeTitle() != null && !notice.getNoticeTitle().trim().isEmpty()) {
                existingNotice.setNoticeTitle(notice.getNoticeTitle());
            }

            if (notice.getNoticeContent() != null && !notice.getNoticeContent().trim().isEmpty()) {
                existingNotice.setNoticeContent(notice.getNoticeContent());
            }

            if (notice.getNoticeCategory() != null && !notice.getNoticeCategory().trim().isEmpty()) {
                existingNotice.setNoticeCategory(notice.getNoticeCategory());
            }

            NoticeEntity updatedNotice = noticeService.updateNotice(existingNotice);
            return ResponseEntity.ok(updatedNotice);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long seq, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        NoticeEntity notice = noticeService.getNoticeBySeq(seq);
        if (!notice.getAdminId().equals(user.getUserNickName())) {
            return ResponseEntity.status(403).build();
        }

        noticeService.deleteNotice(seq);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/increment-views/{seq}")
    public ResponseEntity<Void> incrementViews(@PathVariable Long seq) {
        noticeService.incrementViews(seq);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/category")
    public Page<NoticeEntity> getNoticesByCategory(@RequestParam String category, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeService.getNoticesByCategory(category, pageable);
    }
}
