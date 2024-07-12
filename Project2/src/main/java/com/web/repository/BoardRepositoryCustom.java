package com.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.entity.Board;

public interface BoardRepositoryCustom {
	Page<Board> findAllBoards(Pageable pageable);

	Page<Board> searchBoardsByTitle(String keyword, Pageable pageable);

	Page<Board> findBoardsByCategory(String category, Pageable pageable); // 추가된 메서드

	Page<Board> findRecentBoards(Pageable pageable);
}
