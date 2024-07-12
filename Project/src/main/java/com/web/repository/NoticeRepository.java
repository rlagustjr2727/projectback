//package com.web.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.web.notice.NoticeEntity;
//
//public interface NoticeRepository extends JpaRepository<NoticeEntity, Long>, NoticeRepositoryCustom{
//
//	Page<NoticeEntity> findAllByOrderByNoticeDateTimeDesc(Pageable pageable);
//	
//}
