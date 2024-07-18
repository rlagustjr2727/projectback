package com.web.service;

import java.util.List;

import com.web.entity.Board;
import com.web.entity.Comment;

public interface MyPageService {
    List<Board> getBoardsByAuthor(String author);
    List<Comment> getCommentsByAuthor(String author);
    List<Board> getLikedBoards(String userId);
}