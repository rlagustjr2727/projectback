package com.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor // 매개변수가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 생성
@Table(name = "search")
public class Search {

	// 검색에서 id 가 필요한 이유는 검색 기록을 개별적으로 관리하기 어렵고 
	// 동일한 검색어에 대한 중복 데이터가 발생하기 때문
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long searchid;
	
	@Column(name = "search_keyword", nullable = false)
	private String searchKeyword;
	
	@Column(name = "search_count", nullable = false)
	private int searchCount;
	
	// 추천 검색어
	@Column(name = "search_recommned")
	private String searchRecommend;
	
	// 인기 검색어
	@Column(name = "search_popular")
	private String searchPopular;
	
	// 검색 히스토리를 저장하고 이를 바탕으로 인기 검색어나 추천 검색어를 제공하고자하면 날짜가 유용할 수 있음
	// ex) 특정 기간 동안의 인기 검색어 조회 가능
	//     - 최근 한 달 동안 가장 많이 검색된 키워드 찾기
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();   
}
