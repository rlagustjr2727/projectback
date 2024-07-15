package com.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
	
	Page<Board> findAllByOrderByBoardDateDesc(Pageable pageable);
	
	List<Board> findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(String title, String content);
}
