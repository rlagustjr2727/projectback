package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.entity.Wboard;

public interface WboardRepository extends JpaRepository<Wboard, String>{

}
