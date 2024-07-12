package com.example.demo;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.entity.Board;
import com.web.service.BoardService;

@SpringBootTest
public class BoardTest {

//    @Autowired
//    private BoardService boardService;
//
//    @Test
//    void testJpa() {
//        for (int i = 1; i <= 100; i++) {
//            String title = String.format("테스트 데이터입니다: [%03d]", i);
//            String category = "Category " + (i % 5 + 1); // 1부터 5까지의 카테고리
//            int views = 0;
//            String author = "Author " + (i % 10 + 1); // 1부터 10까지의 작성자
//            String content = "내용무";
//            Board board = new Board();
//            board.setTitle(title);
//            board.setCategory(category);
//            board.setViews(views);
//            board.setAuthor(author);
//            board.setContent(content);
//            boardService.createBoard(board);
//        }
//    }
}
