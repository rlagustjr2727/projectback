package com.web.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.web.entity.Board;
import com.web.entity.User;

@Repository
public class FavoriteRepositoryCustomImpl implements FavoriteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByUserAndBoard(User user, Board board) {
        String queryStr = "SELECT COUNT(*) FROM favorites WHERE user_id = :userId AND board_id = :boardId";
        Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("userId", user.getUserId());
        query.setParameter("boardId", board.getBoardSeq());
        long count = ((Number) query.getSingleResult()).longValue();
        return count > 0;
    }
}
