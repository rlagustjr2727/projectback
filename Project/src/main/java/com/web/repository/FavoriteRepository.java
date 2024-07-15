package com.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.entity.Board;
import com.web.entity.Favorite;
import com.web.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
    boolean existsByUserAndBoard(User user, Board board);
    void deleteByUserAndBoard(User user, Board board);
}
