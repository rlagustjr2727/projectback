package com.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.entity.Board;
import com.web.entity.Comment;
import com.web.entity.User;
import com.web.service.MyPageService;

@RestController
@RequestMapping("/api/mypage")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;

    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getBoardsByAuthor(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }
        List<Board> boards = myPageService.getBoardsByAuthor(user.getUserNickName());
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getCommentsByAuthor(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }
        List<Comment> comments = myPageService.getCommentsByAuthor(user.getUserNickName());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/liked-boards")
    public ResponseEntity<List<Board>> getLikedBoards(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }
        List<Board> likedBoards = myPageService.getLikedBoards(user.getUserId());
        return ResponseEntity.ok(likedBoards);
    }
}

