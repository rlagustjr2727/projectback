package com.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserIdAndUserPassword(String userId, String userPassword);
}
