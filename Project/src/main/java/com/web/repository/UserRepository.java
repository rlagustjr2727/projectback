package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    User findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByUserNickName(String userNickName);
    
    List<User> findByUserNameContainingIgnoreCaseOrUserNickNameContainingIgnoreCase(String name, String nickname);
}
