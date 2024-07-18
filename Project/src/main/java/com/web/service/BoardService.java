package com.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.web.entity.Board;
import com.web.entity.Favorite;
import com.web.entity.User;
import com.web.exception.ResourceNotFoundException;
import com.web.repository.BoardRepository;
import com.web.repository.BoardRepositoryCustom;
import com.web.repository.FavoriteRepository;
import com.web.repository.UserRepository;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private SearchService searchService;

    @Autowired
    @Qualifier("boardRepositoryCustomImpl")
    private BoardRepositoryCustom boardRepositoryCustom;
    
    public List<Board> searchBoards(String query) {
        return boardRepository.findByBoardTitleContainingOrBoardContentContaining(query, query);
    }

    public List<Board> searchCustomRecommendations(String query) {
        return boardRepository.findByCategoryAndKeyword("맞춤추천", query);
    }

    public List<Board> searchAll(String query) {
        return boardRepository.findByKeyword(query);
    }

    public List<Board> getRecentBoards() {
        Page<Board> recentBoardsPage = boardRepository.findRecentBoards(PageRequest.of(0, 6));
        return recentBoardsPage.getContent();
    }
    
    public List<Board> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    public Page<Board> getAllBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.findAllBoards(pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }

    public Board getBoardBySeq(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setCommentCount(board.getCommentCount());
        User author = userRepository.findById(board.getBoardAuthor()).orElse(null);
        if (author != null) {
            board.setBoardProfileImage(author.getUserProfileImage());
        }
        return board;
    }

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board updateBoard(Board updatedBoard) {
        Board existingBoard = boardRepository.findById(updatedBoard.getBoardSeq())
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + updatedBoard.getBoardSeq()));

        existingBoard.setBoardTitle(updatedBoard.getBoardTitle());
        existingBoard.setBoardCategory(updatedBoard.getBoardCategory());
        existingBoard.setBoardContent(updatedBoard.getBoardContent());
        existingBoard.setBoardImage(updatedBoard.getBoardImage());

        return boardRepository.save(existingBoard);
    }

    public void deleteBoard(Long seq) {
        boardRepository.deleteById(seq);
    }
    
    // 페이징
    public Page<Board> searchBoardsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.searchBoardsByTitle(title, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    // 조회수 증가
    public void incrementViews(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardViews(board.getBoardViews() + 1);
        boardRepository.save(board);
    }
    
    // 카테고리별 게시글을 가져오는 메서드
    public Page<Board> getBoardsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.findBoardsByCategory(category, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    // 좋아요 수 증가 메서드 추가
    @Transactional
    public void likeBoard(Long seq, User user) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        if (favoriteRepository.existsByUserAndBoard(user, board)) {
            throw new IllegalStateException("Already liked this board");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBoard(board);
        favoriteRepository.save(favorite);
        board.setBoardLikes(board.getBoardLikes() + 1);
        boardRepository.save(board);
    }

    @Transactional
    public void unlikeBoard(Long seq, User user) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        favoriteRepository.deleteByUserAndBoard(user, board);
        board.setBoardLikes(board.getBoardLikes() - 1);
        boardRepository.save(board);
    }
    
    
}
