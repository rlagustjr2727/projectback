package com.web.repository;

public interface UserRepositoryCustom {
    boolean existsByUserNickNameCustom(String userNickName);
    boolean existsByUserIdCustom(String userId); // 새 메서드 추가
}
