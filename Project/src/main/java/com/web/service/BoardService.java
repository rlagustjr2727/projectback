package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.web.entity.Board;
import com.web.entity.User;
import com.web.exception.ResourceNotFoundException;
import com.web.repository.BoardRepository;
import com.web.repository.BoardRepositoryCustom;
import com.web.repository.UserRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("boardRepositoryCustomImpl")
    private BoardRepositoryCustom boardRepositoryCustom;

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

    public Page<Board> searchBoardsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.searchBoardsByTitle(title, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }

    public void incrementViews(Long seq) {
        Board board = boardRepository.findById(seq)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardViews(board.getBoardViews() + 1);
        boardRepository.save(board);
    }

    public Page<Board> getBoardsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.findBoardsByCategory(category, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }

    public void likeBoard(Long seq) {
        Board board = boardRepository.findById(seq)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardLikes(board.getBoardLikes() + 1);
        boardRepository.save(board);
    }

    public void unlikeBoard(Long seq) {
        Board board = boardRepository.findById(seq)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardLikes(board.getBoardLikes() - 1);
        boardRepository.save(board);
    }
}
