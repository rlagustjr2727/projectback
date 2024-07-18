package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.entity.Wboard;

public interface WboardRepository extends JpaRepository<Wboard, String> {

//	boolean existsByWboardName(String wboardName); // 중복 체크


    @Query("SELECT w FROM Wboard w WHERE w.wboardName LIKE %:keyword% OR w.wboardInfo LIKE %:keyword% OR w.wboardTip LIKE %:keyword%")
    List<Wboard> findByKeyword(@Param("keyword") String keyword);
}
