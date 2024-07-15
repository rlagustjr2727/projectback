// CommentController.java
package com.web.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.entity.Comment;
import com.web.entity.User;
import com.web.service.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/board/{boardSeq}")
    public List<Comment> getCommentsByBoardSeq(@PathVariable Long boardSeq) {
        return commentService.getCommentsByBoardSeq(boardSeq);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(HttpSession session, @RequestBody Comment comment) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        comment.setCommentAuthor(user.getUserNickName());
        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{commentSeq}")
    public ResponseEntity<Void> deleteComment(HttpSession session, @PathVariable Long commentSeq) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            commentService.deleteComment(commentSeq, user.getUserNickName());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{commentSeq}")
    public ResponseEntity<Comment> updateComment(HttpSession session, @PathVariable Long commentSeq, @RequestBody Comment updatedComment) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            Comment comment = commentService.updateComment(commentSeq, updatedComment, user.getUserNickName());
            return ResponseEntity.ok(comment);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
