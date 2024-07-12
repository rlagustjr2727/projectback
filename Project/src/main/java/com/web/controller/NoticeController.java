//package com.web.controller;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.web.notice.NoticeEntity;
//import com.web.service.NoticeService;
//
//@RestController
//@RequestMapping("/api/notice")
//public class NoticeController {
//
//    @Autowired
//    private NoticeService noticeService;
//
//    // 업로드된 파일을 저장할 폴더 경로
//    private static final String UPLOADED_FOLDER = "uploads/notice";
//
//    @GetMapping
//    public List<NoticeEntity> getAllNotices() {
//        return noticeService.getAllNotice();
//    }
//
//    @GetMapping("/list")
//    public Page<NoticeEntity> getAllNotices(Pageable pageable) {
//        return noticeService.getAllNotices(pageable);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<NoticeEntity> getNoticeById(@PathVariable Long id) {
//        Optional<NoticeEntity> notice = noticeService.getNoticeById(id);
//        return notice.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<NoticeEntity> createNotice(@RequestPart("notice") NoticeEntity notice,
//                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
//        try {
//            if (image != null && !image.isEmpty()) {
//                byte[] bytes = image.getBytes();
//                Path path = Paths.get(UPLOADED_FOLDER + StringUtils.cleanPath(image.getOriginalFilename()));
//
//                // 디렉토리가 존재하지 않으면 생성
//                if (Files.notExists(path.getParent())) {
//                    Files.createDirectories(path.getParent());
//                }
//
//                Files.write(path, bytes);
//
//                // 이미지 URL을 설정
//                notice.setNoticeContentImage("/" + UPLOADED_FOLDER + image.getOriginalFilename());
//            } else {
//                notice.setNoticeContentImage(null); // 이미지가 없을 경우 null로 설정
//            }
//            notice.setNoticeDateTime(new Date()); // 현재 날짜로 설정
//
//            NoticeEntity savedNotice = noticeService.createNotice(notice, image);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<NoticeEntity> updateNotice(@PathVariable Long id,
//                                                     @RequestPart("notice") NoticeEntity notice,
//                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
//        try {
//            if (image != null && !image.isEmpty()) {
//                byte[] bytes = image.getBytes();
//                Path path = Paths.get(UPLOADED_FOLDER + StringUtils.cleanPath(image.getOriginalFilename()));
//
//                // 디렉토리가 존재하지 않으면 생성
//                if (Files.notExists(path.getParent())) {
//                    Files.createDirectories(path.getParent());
//                }
//
//                Files.write(path, bytes);
//
//                // 이미지 URL을 설정
//                notice.setNoticeContentImage("/" + UPLOADED_FOLDER + image.getOriginalFilename());
//            } // 이미지가 없을 경우 변경하지 않음
//
//            notice.setNoticeDateTime(new Date()); // 현재 날짜로 설정
//
//            NoticeEntity updatedNotice = noticeService.updateNotice(id, notice, image);
//            return ResponseEntity.ok(updatedNotice);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
//        noticeService.deleteNotice(id);
//        return ResponseEntity.noContent().build();
//    }
//    
//}
