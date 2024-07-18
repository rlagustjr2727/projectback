package com.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

	Page<Board> findAllByOrderByBoardDateDesc(Pageable pageable);

	List<Board> findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(String title, String content);

	List<Board> findByBoardAuthor(String author); // 마이페이지 게시글 목록

	@Query("SELECT f.board FROM Favorite f WHERE f.user.userId = :userId")
	List<Board> findLikedBoardsByUserId(@Param("userId") String userId); // 마이페이지 좋아요한 게시글 목록

	List<Board> findByBoardTitleContainingOrBoardContentContaining(String boardTitle, String boardContent);

	@Query("SELECT b FROM Board b WHERE b.boardCategory = :category AND (b.boardTitle LIKE %:keyword% OR b.boardContent LIKE %:keyword%)")
    List<Board> findByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword);

    @Query("SELECT b FROM Board b WHERE b.boardTitle LIKE %:keyword% OR b.boardContent LIKE %:keyword%")
    List<Board> findByKeyword(@Param("keyword") String keyword);
}
