package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Comment;
import com.example.saenaljigi.domain.Post;
import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.repository.CommentRepository;
import com.example.saenaljigi.repository.PostRepository;
import com.example.saenaljigi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    public ResponseEntity<Void> create(Long userId, Long postId, String content,boolean isAnonymous){
        //Id 찾기
        User user = userRepository.findById(userId).orElseThrow();
        //post찾기
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = Comment.builder().
                post(post).
                anonymousName(isAnonymous).
                content(content).
                createdAt(LocalDateTime.now()).build();



        //레포지토리에 저장하기
        commentRepository.save(comment);

        return ResponseEntity.ok().build();

    }
}
