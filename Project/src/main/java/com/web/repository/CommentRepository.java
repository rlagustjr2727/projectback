package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.entity.Comment;



public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByBoardBoardSeq(Long boardSeq);
    
    @Query("SELECT c FROM Comment c JOIN FETCH c.board WHERE c.commentAuthor = :author")
    List<Comment> findByCommentAuthor(@Param("author") String author); // 마이페이지 댓글작성 목록

}