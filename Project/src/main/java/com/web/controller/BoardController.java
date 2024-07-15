package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Board;
import com.web.entity.User;
import com.web.entity.User;
import com.web.service.BoardService;

@RestController
@RequestMapping("/api/board")
@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    @Autowired
    private BoardService boardService;

    private static final String UPLOADED_FOLDER = "uploads/";

    @GetMapping("/recent")
    public List<Board> getRecentBoards() {
        return boardService.getRecentBoards();
    }


    @GetMapping("/all")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping
    public Page<Board> getAllBoards(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getAllBoards(page, size);
    }

    @GetMapping("/{seq}")
    public Board getBoardBySeq(@PathVariable Long seq) {
        return boardService.getBoardBySeq(seq);
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(
            @RequestPart("board") Board board,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);
                board.setBoardImage(UPLOADED_FOLDER + image.getOriginalFilename());
            } else {
                board.setBoardImage(null);
            }
            board.setBoardDate(new Date());
            board.setBoardAuthor(user.getUserNickName());
            board.setBoardProfileImage(user.getUserProfileImage());
            board.setBoardAuthor(user.getUserNickName());
            board.setBoardProfileImage(user.getUserProfileImage());

            Board savedBoard = boardService.saveBoard(board);
            return ResponseEntity.ok(savedBoard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{seq}")
    public ResponseEntity<Board> updateBoard(
            @PathVariable Long seq,
            @RequestPart("board") Board board,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        try {
            Board existingBoard = boardService.getBoardBySeq(seq);

            if (!existingBoard.getBoardAuthor().equals(user.getUserNickName())) {
            if (!existingBoard.getBoardAuthor().equals(user.getUserNickName())) {
                return ResponseEntity.status(403).build();
            }

            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);
                existingBoard.setBoardImage(UPLOADED_FOLDER + image.getOriginalFilename());
            }

            if (board.getBoardTitle() != null && !board.getBoardTitle().trim().isEmpty()) {
                existingBoard.setBoardTitle(board.getBoardTitle());
            }

            if (board.getBoardContent() != null && !board.getBoardContent().trim().isEmpty()) {
                existingBoard.setBoardContent(board.getBoardContent());
            }

            if (board.getBoardCategory() != null) {
                existingBoard.setBoardCategory(board.getBoardCategory());
            }

            Board updatedBoard = boardService.updateBoard(existingBoard);
            return ResponseEntity.ok(updatedBoard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long seq, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

    public ResponseEntity<Void> deleteBoard(@PathVariable Long seq, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }

        Board board = boardService.getBoardBySeq(seq);
        if (!board.getBoardAuthor().equals(user.getUserNickName())) {
            return ResponseEntity.status(403).build();
        if (!board.getBoardAuthor().equals(user.getUserNickName())) {
            return ResponseEntity.status(403).build();
        }

        boardService.deleteBoard(seq);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Board> searchBoardsByTitle(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.searchBoardsByTitle(keyword, page, size);
    }


    @PostMapping("/increment-views/{seq}")
    public ResponseEntity<Void> incrementViews(@PathVariable Long seq) {
        boardService.incrementViews(seq);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/category")
    public Page<Board> getBoardsByCategory(@RequestParam("category") String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardsByCategory(category, page, size);
    }


    @PostMapping("/like/{seq}")
    public ResponseEntity<Void> likeBoard(@PathVariable Long seq) {
        boardService.likeBoard(seq);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/unlike/{seq}")
    public ResponseEntity<Void> unlikeBoard(@PathVariable Long seq) {
        boardService.unlikeBoard(seq);
        return ResponseEntity.ok().build();
    }
}
