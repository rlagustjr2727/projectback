package com.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{


}
