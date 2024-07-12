//package com.web.notice;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "notice")
//public class NoticeEntity {
//
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long noticeSeq;
//	
//	@Column(name= "admin_id", nullable = false)
//	private String adminId;
//	
//	@Column(name= "notice_title", nullable = false)
//	private String noticeTitle;
//	
//	@Column(name= "notice_content", nullable = false)
//	private String noticeContent;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "notice_date_time", nullable = false)
//	private Date noticeDateTime = new Date();
//	
//	@Column(name = "notice_content_image")
//	private String noticeContentImage;
//	
//	@Column(name = "notice_view-count", nullable = false)
//	private int noticeViewCount;
//}
