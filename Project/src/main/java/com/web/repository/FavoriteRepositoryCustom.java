package com.web.repository;

import com.web.entity.Board;
import com.web.entity.User;

public interface FavoriteRepositoryCustom {
    boolean existsByUserAndBoard(User user, Board board);
}
