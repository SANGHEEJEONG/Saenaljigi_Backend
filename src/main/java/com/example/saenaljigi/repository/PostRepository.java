package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByUserId(Long userId);
    List<Post> findAllByOrderByCommentCntDesc();
}