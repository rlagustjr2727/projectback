package com.web.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public boolean existsByUserNickNameCustom(String userNickName) {
        String queryStr = "SELECT 1 FROM USERS WHERE USER_NICKNAME = :userNickName AND ROWNUM = 1";
        Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("userNickName", userNickName);
        
        return !query.getResultList().isEmpty();
    }

    // 추가: 사용자 ID가 존재하는지 확인하는 커스텀 메서드
    @Override
    public boolean existsByUserIdCustom(String userId) {
        String queryStr = "SELECT 1 FROM USERS WHERE USER_ID = :userId AND ROWNUM = 1";
        Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("userId", userId);
        
        return !query.getResultList().isEmpty();
    }
}