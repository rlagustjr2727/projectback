package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.Board;
import com.web.entity.Comment;
import com.web.repository.BoardRepository;
import com.web.repository.CommentRepository;

@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public List<Board> getBoardsByAuthor(String author) {
        return boardRepository.findByBoardAuthor(author);
    }

    @Override
    public List<Comment> getCommentsByAuthor(String author) {
        List<Comment> comments = commentRepository.findByCommentAuthor(author);
        comments.forEach(comment -> System.out.println("Fetched comment with board: " + comment.getBoard()));
        return comments;
    }

    @Override
    public List<Board> getLikedBoards(String userId) {
        return boardRepository.findLikedBoardsByUserId(userId);
    }
}
