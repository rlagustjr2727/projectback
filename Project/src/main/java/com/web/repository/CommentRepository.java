package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.entity.Comment;



public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByBoardBoardSeq(Long boardSeq);
}