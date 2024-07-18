package com.web.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
	@SequenceGenerator(name = "board_seq", sequenceName = "board_seq", allocationSize = 1)
	private Long boardSeq;

	@Column(name = "board_title", nullable = false)
	private String boardTitle;

	@Column(name = "board_category", nullable = false)
	private String boardCategory;

	@Column(name = "board_views", nullable = false)
	private Integer boardViews;

	@Column(name = "board_author", nullable = false)
	private String boardAuthor;

	@Column(name = "board_content", nullable = false)
	private String boardContent;

	@Column(name = "board_image")
	private String boardImage;

	// DB 에 추가
	@Column(name = "board_profile_image")
	private String boardProfileImage;

	@Temporal(TemporalType.DATE)
	@Column(name = "board_date")
	private Date boardDate = new Date(); // 기본값을 현재 날짜로 설정

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("board-comments")
	private List<Comment> comments;
	
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("board-favorites")
    private List<Favorite> favorites;

	@Transient
	private int commentCount;

	@Column(name = "board_likes", nullable = false)
	private int boardLikes = 0;

	public int getCommentCount() {
		return comments != null ? comments.size() : 0;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setBoard(this);
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		comment.setBoard(null);
	}
}
