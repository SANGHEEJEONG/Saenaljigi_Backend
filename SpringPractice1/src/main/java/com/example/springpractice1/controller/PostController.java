package com.example.springpractice1.controller;

import com.example.springpractice1.entity.Post;
import com.example.springpractice1.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public Post createPost(Long userId, String title, String content, String writer){
        return postService.createPost(userId,title,content);
    }
}
